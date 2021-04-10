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

package com.dobrovolskis.commexp.web.usecase.item

import com.dobrovolskis.commexp.exception.ResourceAccessError
import com.dobrovolskis.commexp.model.PurchaseItem
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.service.InvoiceService
import com.dobrovolskis.commexp.service.PurchaseService
import com.dobrovolskis.commexp.web.request.ItemCreationRequest
import com.dobrovolskis.commexp.web.usecase.BaseRequestHandler
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.06
 */
@Service
@Transactional
class AddPurchaseItem(
	private val purchaseService: PurchaseService,
	private val invoiceService: InvoiceService,
) : BaseRequestHandler<ItemCreationRequest, PurchaseItem> {

	override operator fun invoke(
		currentUser: User,
		request: ItemCreationRequest
	): PurchaseItem {
		validateRequest(currentUser, request)

		val purchase = purchaseService.find(request.purchaseId)
		val item = purchaseService.addItem(purchase, request.name, request.price)

		invoiceService.reassembleForChangedPurchase(purchase)
		return item
	}

	private fun validateRequest(user: User, request: ItemCreationRequest) {
		val purchase = purchaseService.find(request.purchaseId)
		if (!purchase.group.containsUser(user)) {
			throw ResourceAccessError("Access to purchase ${request.purchaseId} denied")
		}
	}
}
