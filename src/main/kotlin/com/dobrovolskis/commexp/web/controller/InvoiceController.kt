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

import com.dobrovolskis.commexp.config.DATE_FORMAT
import com.dobrovolskis.commexp.config.PATH_INVOICES
import com.dobrovolskis.commexp.model.Invoice
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.web.ControllerUtils
import com.dobrovolskis.commexp.web.dto.InvoiceDto
import com.dobrovolskis.commexp.web.request.InvoiceAssemblyRequest
import com.dobrovolskis.commexp.web.usecase.invoice.AssembleInvoices
import com.dobrovolskis.commexp.web.usecase.invoice.QueryInvoices
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.util.UUID
import javax.validation.Valid

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.14
 */
@RestController
@RequestMapping(value = [PATH_INVOICES])
class InvoiceController(
	private val assembleInvoices: AssembleInvoices,
	private val queryInvoices: QueryInvoices,
	private val controllerUtils: ControllerUtils,
) {

	@RequestMapping(method = [POST])
	fun assembleAllForGroup(@RequestBody @Valid request: InvoiceAssemblyRequest): List<InvoiceDto> {
		val user = getUser()
		return assembleInvoices(user, request).map { mapToDto(it) }
	}

	@RequestMapping(method = [GET])
	fun getAllForGroup(
		@RequestParam(required = true) groupId: UUID,
		@RequestParam(required = true) from: String,
		@RequestParam(required = true) to: String
	): List<InvoiceDto> {
		val user = getUser()
		val request = InvoiceAssemblyRequest(
			groupId = groupId,
			start = LocalDate.from(DATE_FORMAT.parse(from)),
			end = LocalDate.from(DATE_FORMAT.parse(to))
		)
		return queryInvoices.invoke(user, request = request).map { mapToDto(it) }
	}

	private fun mapToDto(invoice: Invoice) = InvoiceDto(
		id = invoice.id()!!,
		payerId = invoice.payer.id()!!,
		receiverId = invoice.receiver.id()!!,
		groupId = invoice.group.id()!!,
		from = invoice.from,
		to = invoice.to,
		sum = invoice.sum
	)

	private fun getUser(): User = controllerUtils.getCurrentUser()
}
