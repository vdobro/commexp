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

package com.dobrovolskis.commexp.service

import com.dobrovolskis.commexp.model.Invoice
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.model.UserGroup
import com.dobrovolskis.commexp.repository.InvoiceRepository
import com.dobrovolskis.commexp.repository.PurchaseItemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.14
 */
@Service
@Transactional
class InvoiceService(
	private val invoiceRepository: InvoiceRepository,
	private val itemRepository: PurchaseItemRepository
) {
	fun assembleForUser(
		user: User, group: UserGroup,
		from: ZonedDateTime, to: ZonedDateTime
	): Iterable<Invoice> {
		validateRequest(user = user, group = group, from = from, to = to)
		val invoices = itemRepository.getUsedUpItemsByPurchaseDoneWithin(
			from = from, until = to,
			usedBy = user, group = group
		)
			.groupBy { item -> item.purchase.doneBy }
			.mapKeys { (buyer, items) ->
				Invoice(from = from, to = to,
					payer = user, receiver = buyer, group = group,
					paymentDueCents = items.sumBy { item ->
						item.priceCents / item.usedBy().size
					})
			}
			.keys.toList()
		return invoiceRepository.saveAll(invoices)
	}

	private fun validateRequest(
		user: User, group: UserGroup,
		from: ZonedDateTime, to: ZonedDateTime
	) {
		require(user.isInGroup(group)) {
			"User ${user.name} not in group ${group.name}"
		}
		require(from.isBefore(to) || from.isEqual(to)) {
			"Invalid date range"
		}
		checkDate("Start", from, user, group)
		checkDate("End", to, user, group)
	}

	private fun checkDate(
		name: String, date: ZonedDateTime,
		user: User, group: UserGroup
	) {
		val overlapping = invoiceRepository.findAllWithDateWithinBounds(
			date = date,
			payer = user,
			group = group
		)
		require(overlapping.isEmpty()) {
			val invoice = overlapping.first().toString()
			"$name date overlaps with $invoice"
		}
	}
}