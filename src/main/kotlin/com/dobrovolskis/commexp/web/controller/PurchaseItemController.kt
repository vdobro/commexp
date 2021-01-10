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

package com.dobrovolskis.commexp.web.controller

import com.dobrovolskis.commexp.config.PATH_PURCHASE_ITEMS
import com.dobrovolskis.commexp.model.PurchaseItem
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.web.ControllerUtils
import com.dobrovolskis.commexp.web.dto.PurchaseItemDto
import com.dobrovolskis.commexp.web.request.ItemCreationRequest
import com.dobrovolskis.commexp.web.request.ItemListRequest
import com.dobrovolskis.commexp.web.request.ItemUsageChangeRequest
import com.dobrovolskis.commexp.web.usecase.item.AddPurchaseItem
import com.dobrovolskis.commexp.web.usecase.item.GetItemsInPurchase
import com.dobrovolskis.commexp.web.usecase.item.MarkItemAsUsedUp
import com.dobrovolskis.commexp.web.usecase.item.RemovePurchaseItem
import com.dobrovolskis.commexp.web.usecase.item.UpdateItemUsageByUser
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.DELETE
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.06
 */
@RestController
@RequestMapping(value = [PATH_PURCHASE_ITEMS])
class PurchaseItemController(
	private val addItem: AddPurchaseItem,
	private val getAllItems: GetItemsInPurchase,
	private val removeItem: RemovePurchaseItem,
	private val updateItemUsage: UpdateItemUsageByUser,
	private val markItemAsUsedUp: MarkItemAsUsedUp,
	private val controllerUtils: ControllerUtils,
) {

	@RequestMapping(method = [POST])
	fun addNewItem(@RequestBody itemCreationRequest: ItemCreationRequest): PurchaseItemDto {
		val result = addItem(getUser(), itemCreationRequest)
		return mapToDto(result)
	}

	@RequestMapping(method = [GET])
	fun getAllInPurchase(@RequestBody request: ItemListRequest): List<PurchaseItemDto> {
		val result = getAllItems(getUser(), request)
		return result.map(this::mapToDto)
	}

	@RequestMapping(
		method = [DELETE],
		path = ["/{id}"]
	)
	fun removeExistingItem(@PathVariable id: UUID) {
		removeItem(getUser(), id)
	}

	@RequestMapping(
		method = [POST],
		path = ["/{id}/use/by"]
	)
	fun editItemUsage(
		@PathVariable id: UUID,
		@RequestBody request: ItemUsageChangeRequest
	): PurchaseItemDto {
		val result = updateItemUsage(getUser(), request)
		return mapToDto(result)
	}

	@RequestMapping(
		method = [POST],
		path = ["/{id}/use/up"]
	)
	fun markAsUsedUp(@PathVariable id: UUID): PurchaseItemDto {
		val result = markItemAsUsedUp(getUser(), request = id)
		return mapToDto(result)
	}

	private fun mapToDto(purchaseItem: PurchaseItem): PurchaseItemDto {
		return PurchaseItemDto(
			id = purchaseItem.id()!!,
			name = purchaseItem.name,
			purchaseId = purchaseItem.purchase.id()!!,
			price = purchaseItem.price
		)
	}

	private fun getUser(): User = controllerUtils.getCurrentUser()
}
