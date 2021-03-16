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

package com.dobrovolskis.commexp.service

import com.dobrovolskis.commexp.model.BatchImport
import com.dobrovolskis.commexp.model.IdEntity
import com.dobrovolskis.commexp.model.ImportedEntity
import com.dobrovolskis.commexp.model.ImportedEntityType
import com.dobrovolskis.commexp.model.ImportedEntityType.ITEM
import com.dobrovolskis.commexp.model.ImportedEntityType.PURCHASE
import com.dobrovolskis.commexp.model.Purchase
import com.dobrovolskis.commexp.model.PurchaseItem
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.repository.BatchImportRepository
import com.dobrovolskis.commexp.repository.ImportedEntityRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.03.12
 */
@Service
@Transactional
class BatchImportService(
	private val batchImportRepository: BatchImportRepository,
	private val entityRepository: ImportedEntityRepository,
	private val purchaseService: PurchaseService,
) {
	fun createImportSession(author: User): BatchImport = batchImportRepository.save(BatchImport(importedBy = author))

	fun import(
		import: BatchImport,
		purchase: Purchase,
		originalId: String
	): ImportedEntity {
		val persistedPurchase = purchaseService.createNew(
			creator = purchase.createdBy,
			user = purchase.doneBy,
			group = purchase.group,
			shop = purchase.shop,
			shoppingTime = purchase.shoppingTime,
		)
		return addEntity(import, persistedPurchase, PURCHASE, originalId)
	}

	fun import(
		import: BatchImport,
		item: PurchaseItem,
		originalId: String
	): ImportedEntity {
		val persistedItem = purchaseService.addItem(
			purchase = item.purchase,
			itemName = item.name,
			price = item.price,
			description = item.description,
		)
		return addEntity(import, persistedItem, ITEM, originalId)
	}

	fun findPurchase(originalId: String) : Purchase {
		val entity = entityRepository.findFirstByOriginalId(originalId)
			?: throw IllegalArgumentException("Imported purchase not found")
		require(entity.type == PURCHASE) {
			"Imported entity $originalId is not a purchase"
		}
		return purchaseService.find(entity.entityId)
	}

	private fun addEntity(
		import: BatchImport,
		entity: IdEntity,
		type: ImportedEntityType,
		originalId: String
	): ImportedEntity {
		val importedItem = ImportedEntity(
			import = import,
			type = type,
			entityId = entity.id()!!,
			originalId = originalId
		)
		import.addItem(importedItem)
		batchImportRepository.save(import)
		return importedItem
	}
}
