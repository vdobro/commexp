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

import com.dobrovolskis.commexp.controller.dto.InvoiceDto
import com.dobrovolskis.commexp.controller.request.InvoiceRequest
import com.dobrovolskis.commexp.controller.usecase.AssembleInvoices
import com.dobrovolskis.commexp.model.Invoice
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.14
 */
@RestController
@RequestMapping("/invoices")
class InvoiceController(
	private val assembleInvoices: AssembleInvoices) {

	@RequestMapping(method = [RequestMethod.POST])
	fun requestInvoice(@RequestBody request: InvoiceRequest): List<InvoiceDto> {
		val user = getCurrentUser()
		return assembleInvoices(user, request).map { mapToDto(it) }
	}

	private fun mapToDto(invoice: Invoice) : InvoiceDto {
		return InvoiceDto(
			id = invoice.id()!!,
			payerId = invoice.payer.id()!!,
			receiverId = invoice.receiver.id()!!,
			groupId = invoice.group.id()!!,
			from = invoice.from,
			to = invoice.to,
			sum = invoice.paymentDueCents)
	}
}