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

import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.repository.InvoiceRepository
import com.dobrovolskis.commexp.service.InvoiceService
import com.dobrovolskis.commexp.service.UserGroupService
import com.dobrovolskis.commexp.web.request.InvoiceAssemblyRequest
import com.dobrovolskis.commexp.web.usecase.BaseRequestHandler
import com.dobrovolskis.commexp.web.usecase.verifyAccessToGroup
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.03.17
 */
@Service
@Transactional
class PurgeInvoices(
	private val groupService: UserGroupService,
	private val invoiceService: InvoiceService,
	private val invoiceRepository: InvoiceRepository,
) :
	BaseRequestHandler<InvoiceAssemblyRequest, Unit> {
	override operator fun invoke(currentUser: User, request: InvoiceAssemblyRequest) {
		val group = groupService.find(request.groupId)
		verifyAccessToGroup(currentUser, group)

		val range = DateRange(from = request.start, to = request.end)
		val invoices = invoiceService.getInGroup(group, range)

		invoiceRepository.deleteAll(invoices)
	}
}
