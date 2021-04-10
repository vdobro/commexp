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

package com.dobrovolskis.commexp.web.usecase.invoice

import com.dobrovolskis.commexp.model.Invoice
import com.dobrovolskis.commexp.model.InvoiceItem
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.service.InvoiceService
import com.dobrovolskis.commexp.web.usecase.BaseRequestHandler
import com.dobrovolskis.commexp.web.usecase.verifyAccessToGroup
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.BigDecimal.valueOf
import java.util.UUID

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.03.17
 */
@Service
@Transactional(readOnly = true)
class GetInvoiceDetails(
	private val invoiceService: InvoiceService,
) : BaseRequestHandler<UUID, InvoiceDetails> {
	override fun invoke(currentUser: User, request: UUID): InvoiceDetails {
		val invoice = invoiceService.find(request)
		verifyAccessToGroup(currentUser, invoice.group)

		return mapToDetails(invoice)
	}

	private fun mapToDetails(invoice: Invoice): InvoiceDetails {
		return InvoiceDetails(
			id = invoice.id()!!,
			range = DateRange(from = invoice.from, to = invoice.to),
			payerId = invoice.payer.id()!!,
			receiverId = invoice.receiver.id()!!,
			groupId = invoice.group.id()!!,
			mirrorInvoiceId = invoice.mirror?.id(),
			totalAmount = invoice.total,
			reduction = invoice.reduction ?: valueOf(0),
			finalAmount = invoice.finalAmount,
			items = invoice.items().map { mapItem(it) }
		)
	}

	private fun mapItem(item: InvoiceItem): InvoiceItemDetails {
		val purchaseItem = item.item
		return InvoiceItemDetails(
			itemId = purchaseItem.id()!!,
			name = purchaseItem.name,
			sum = item.sum,
			users = purchaseItem.usedBy().map { it.id()!! },
		)
	}
}

data class InvoiceDetails(
	val id: UUID,
	val range: DateRange,
	val payerId: UUID,
	val receiverId: UUID,
	val groupId: UUID,
	val mirrorInvoiceId: UUID?,

	val totalAmount: BigDecimal,
	val reduction: BigDecimal,
	val finalAmount: BigDecimal,

	val items: List<InvoiceItemDetails>
)

data class InvoiceItemDetails(
	val itemId: UUID,
	val name: String,
	val sum: BigDecimal,
	val users: List<UUID>,
)
