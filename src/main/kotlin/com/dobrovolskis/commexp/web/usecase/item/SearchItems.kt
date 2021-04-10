/*
 * Copyright (C) 2021 Vitalijus Dobrovolskis
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

package com.dobrovolskis.commexp.web.usecase.item

import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.service.IndexedItemSearchService
import com.dobrovolskis.commexp.service.UserGroupService
import com.dobrovolskis.commexp.web.assembler.UserAssembler
import com.dobrovolskis.commexp.web.dto.ItemDetailsDto
import com.dobrovolskis.commexp.web.dto.ItemSearchResultDto
import com.dobrovolskis.commexp.web.request.ItemSearchQuery
import com.dobrovolskis.commexp.web.usecase.BaseRequestHandler
import com.dobrovolskis.commexp.web.usecase.verifyAccessToGroup
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.03.23
 */
@Service
@Transactional(readOnly = true)
class SearchItems(
	private val groupService: UserGroupService,
	private val index: IndexedItemSearchService,
	private val userAssembler: UserAssembler
) : BaseRequestHandler<ItemSearchQuery, ItemSearchResultDto> {
	override operator fun invoke(currentUser: User, request: ItemSearchQuery): ItemSearchResultDto {
		val group = groupService.find(request.groupId)
		verifyAccessToGroup(currentUser, group)

		val queryResult = index.searchFirst(request.text, group)
		return ItemSearchResultDto(
			items = queryResult.map {
				ItemDetailsDto(
					id = it.id()!!,
					usedUp = it.usedUp,
					name = it.name,
					description = it.description,
					shopName = it.purchase.shop.name,
					paidBy = userAssembler.toDto(it.purchase.doneBy),
					shoppingTime = it.purchase.shoppingTime,
					purchaseId = it.purchase.id()!!,
					price = it.price
				)
			},
		)
	}
}
