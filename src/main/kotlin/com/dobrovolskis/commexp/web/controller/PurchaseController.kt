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

import com.dobrovolskis.commexp.config.PATH_PURCHASES
import com.dobrovolskis.commexp.model.Purchase
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.web.ControllerUtils
import com.dobrovolskis.commexp.web.dto.PurchaseDto
import com.dobrovolskis.commexp.web.request.PurchaseCreationRequest
import com.dobrovolskis.commexp.web.request.PurchaseListRequest
import com.dobrovolskis.commexp.web.usecase.purchase.CreatePurchase
import com.dobrovolskis.commexp.web.usecase.purchase.GetPurchasesInGroup
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RestController

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.06
 */
@RestController
@RequestMapping(value = [PATH_PURCHASES])
class PurchaseController(
	private val createPurchase: CreatePurchase,
	private val purchaseList: GetPurchasesInGroup,
	private val controllerUtils: ControllerUtils,
) {
	@RequestMapping(method = [POST])
	fun createNew(@RequestBody creationRequest: PurchaseCreationRequest): PurchaseDto {
		val result = createPurchase(getUser(), creationRequest)
		return mapToDto(result)
	}

	@RequestMapping(method = [GET])
	fun getAll(@RequestBody request: PurchaseListRequest): List<PurchaseDto> {
		val result = purchaseList(getUser(), request)
		return result.map(this::mapToDto)
	}

	private fun mapToDto(purchase: Purchase): PurchaseDto {
		return PurchaseDto(
			id = purchase.id()!!,
			shopId = purchase.shop.id()!!,
			time = purchase.shoppingTime,
			creation = purchase.created,
			doneBy = purchase.doneBy.id()!!
		)
	}

	private fun getUser(): User = controllerUtils.getCurrentUser()
}

