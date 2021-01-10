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

import com.dobrovolskis.commexp.model.Invoice
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.service.InvoiceService
import com.dobrovolskis.commexp.service.UserGroupService
import com.dobrovolskis.commexp.web.request.InvoiceRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.14
 */
@Service
@Transactional
class AssembleInvoices(
	private val groupService: UserGroupService,
	private val invoiceService: InvoiceService
) : BaseRequestHandler<InvoiceRequest, Iterable<Invoice>> {

	override operator fun invoke(currentUser: User, request: InvoiceRequest): Iterable<Invoice> {
		val group = groupService.find(request.groupId)
		invoiceService.assembleForGroup(
			group = group,
			from = request.start,
			to = request.end
		)
		return invoiceService.getPaidBy(
			user = currentUser,
			group = group, from = request.start, to = request.end
		)
	}
}
