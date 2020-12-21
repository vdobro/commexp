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

package com.dobrovolskis.commexp.controller

import com.dobrovolskis.commexp.controller.dto.PurchaseDto
import com.dobrovolskis.commexp.controller.request.PurchaseCreationRequest
import com.dobrovolskis.commexp.controller.usecase.purchase.CreatePurchase
import com.dobrovolskis.commexp.model.Purchase
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.06
 */
@RestController
@RequestMapping("/purchases")
class PurchaseController(
	private val createPurchase: CreatePurchase,
) {
	@RequestMapping(method = [RequestMethod.POST])
	fun createNew(@RequestBody creationRequest: PurchaseCreationRequest): PurchaseDto {
		val result = createPurchase(getCurrentUser(), creationRequest)
		return mapToDto(result)
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
}

