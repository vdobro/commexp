/*
 * Copyright (C) 2020 Vitalijus Dobrovolskis
 *
 * This file is part of commexp.
 *
 * commexp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, version 3 of the License.
 *
 * commexp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with commexp; see the file LICENSE. If not,
 * see <https://www.gnu.org/licenses/>.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package com.dobrovolskis.commexp.web.usecase

import com.dobrovolskis.commexp.model.BatchImport
import com.dobrovolskis.commexp.model.ImportedEntity
import com.dobrovolskis.commexp.model.Purchase
import com.dobrovolskis.commexp.model.PurchaseItem
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.model.UserGroup
import com.dobrovolskis.commexp.service.BatchImportService
import com.dobrovolskis.commexp.service.CsvParserUtils
import com.dobrovolskis.commexp.service.PurchaseEntry
import com.dobrovolskis.commexp.service.PurchaseItemEntry
import com.dobrovolskis.commexp.service.PurchaseItemService
import com.dobrovolskis.commexp.service.ShopService
import com.dobrovolskis.commexp.service.UserGroupService
import com.dobrovolskis.commexp.service.UserService
import com.dobrovolskis.commexp.web.dto.BatchImportResultDto
import com.dobrovolskis.commexp.web.request.ImportRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.InputStream

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.03.16
 */
@Service
@Transactional
class BatchImporter(
	private val userService: UserService,
	private val groupService: UserGroupService,
	private val batchImportService: BatchImportService,
	private val csvParserUtil: CsvParserUtils,
	private val shopService: ShopService,
	private val itemService: PurchaseItemService,
) {

	fun importPurchases(request: ImportRequest, user: User) =
		import(request, user, csvParserUtil::parsePurchases, this::handlePurchaseEntry)

	fun importItems(request: ImportRequest, user: User) =
		import(request, user, csvParserUtil::parseItems, this::handlePurchaseItemEntry)

	private inline fun <reified T> import(
		request: ImportRequest,
		currentUser: User,
		parseEntities: (inputStream: InputStream) -> List<T>,
		handleCsvEntry: (entry: T, creator: User, group: UserGroup, session: BatchImport) -> ImportedEntity
	): BatchImportResultDto {
		val group = groupService.find(request.groupId)
		verifyAccessToGroup(currentUser, group)

		val inputStream = request.file.inputStream
		val entries = parseEntities(inputStream)
		val importSession = batchImportService.createImportSession(currentUser)
		val entities = entries.map {
			val entity = handleCsvEntry(it, currentUser, group, importSession)
			entity.entityId
		}
		return csvParserUtil.mapImportToResultDto(entities, importSession)
	}

	private fun handlePurchaseEntry(
		entry: PurchaseEntry,
		creator: User,
		group: UserGroup,
		session: BatchImport,
	): ImportedEntity {
		val shop = shopService.findOrCreate(name = entry.shop!!, group = group)
		val date = entry.date!!.atStartOfDay()
		val doneBy = userService.findByUsername(username = entry.buyer!!)
		verifyAccessToGroup(doneBy, group)

		return batchImportService.import(
			import = session,
			originalId = entry.id!!,
			purchase = Purchase(
				createdBy = creator,
				doneBy = doneBy,
				group = group,
				shop = shop,
				shoppingTime = date
			)
		)
	}

	private fun handlePurchaseItemEntry(
		entry: PurchaseItemEntry,
		creator: User,
		group: UserGroup,
		session: BatchImport,
	): ImportedEntity {
		val originalPurchaseId = entry.purchaseId!!
		val purchase = batchImportService.findPurchase(originalPurchaseId)
		val price = entry.price!!
		val users = entry.users!!.asMap().mapKeys {
			val userName = it.key.replace("benutzer-", "")
			val user = userService.findByUsername(userName)
			verifyAccessToGroup(user, group)
			user
		}.entries
			.filter { it.value.first() == true }
			.map { it.key }
		verifyAccessToGroup(purchase.doneBy, group)
		val result = batchImportService.import(
			import = session,
			originalId = "N/A",
			item = PurchaseItem(
				purchase = purchase,
				name = entry.name!!,
				price = price,
			)
		)
		val item = itemService.find(result.entityId)
		users.forEach {
			itemService.markAsUsedBy(item, user = it)
		}
		return result
	}
}
