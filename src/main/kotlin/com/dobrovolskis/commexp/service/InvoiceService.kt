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

import com.dobrovolskis.commexp.exception.ResourceAccessError
import com.dobrovolskis.commexp.model.Invoice
import com.dobrovolskis.commexp.model.Purchase
import com.dobrovolskis.commexp.model.PurchaseItem
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.model.UserGroup
import com.dobrovolskis.commexp.repository.InvoiceRepository
import com.dobrovolskis.commexp.repository.PurchaseItemRepository
import com.dobrovolskis.commexp.web.usecase.invoice.DateRange
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.BigDecimal.valueOf
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

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
	fun assembleForGroup(
		group: UserGroup,
		range: DateRange
	) {
		for (user in group.users()) {
			assembleForUser(user, group, range)
		}
		cancelOutPairs(group, range)
	}

	fun find(invoiceId: UUID) : Invoice {
		return invoiceRepository.findByIdOrNull(invoiceId) ?: throw ResourceAccessError()
	}

	fun getInGroup(group: UserGroup, range: DateRange) : Iterable<Invoice> {
		return invoiceRepository.findAllByFromIsGreaterThanEqualAndToLessThanEqualAndGroup(
			group = group, from = range.from, to = range.to
		)
	}

	fun getPaidBy(
		user: User, group: UserGroup,
		range: DateRange, filterRedundant: Boolean
	): Iterable<Invoice> {
		val query = if (filterRedundant) {
			invoiceRepository::findAllByFromIsGreaterThanEqualAndToLessThanEqualAndGroupAndPayerAndMirrorIsNotNull
		} else {
			invoiceRepository::findAllByFromIsGreaterThanEqualAndToLessThanEqualAndGroupAndPayer
		}
		return query(
			range.from, range.to, group, user
		)
	}

	fun reassembleForChangedPurchase(purchase: Purchase) {
		val group = purchase.group
		val time = purchase.shoppingTime

		val invoices = invoiceRepository.findAllWithDateIn(
			date = time, group = group
		)
		if (invoices.isEmpty()) {
			return
		}
		val from = invoices.first().from
		val to = invoices.first().to
		val range = DateRange(from = from, to = to)

		invoiceRepository.deleteAll(invoices)
		assembleForGroup(group, range)
	}

	private fun assembleForUser(
		user: User,
		group: UserGroup,
		range: DateRange,
	) {
		val invoices = calculateInvoicesToBePaidBy(user, group, range) ?: return
		invoiceRepository.saveAll(invoices)
	}

	private fun cancelOutPairs(group: UserGroup, range: DateRange) {
		for (first in group.users()) {
			for (second in group.users()) {
				val outgoing = invoiceRepository.findByFromAndToAndPayerAndReceiverAndGroup(
					payer = first, receiver = second,
					from = range.from, to = range.to, group = group
				)
				val incoming = invoiceRepository.findByFromAndToAndPayerAndReceiverAndGroup(
					payer = second, receiver = first,
					from = range.from, to = range.to, group = group,
				)
				if (outgoing != null && incoming != null) {
					eliminatePair(outgoing, incoming)
				}
			}
		}
	}

	private fun eliminatePair(a: Invoice, b: Invoice) {
		if (a.total >= b.total) {
			reduce(a, b)
		}
		if (a.total <= b.total) {
			reduce(b, a)
		}
	}

	private fun reduce(larger: Invoice, smaller: Invoice) {
		val reduction = smaller.total
		larger.reduction = reduction
		larger.finalAmount = larger.total - reduction
		larger.mirror = smaller

		invoiceRepository.save(larger)
	}

	private fun calculateInvoicesToBePaidBy(
		user: User,
		group: UserGroup,
		range: DateRange,
	): Iterable<Invoice>? {
		validateRequest(range)
		val items = itemRepository.getUsedUpItemsByPurchaseDoneWithin(
			from = range.from,
			until = range.to,
			usedBy = user,
			group = group
		)
		if (items.isEmpty()) return null

		val invoices = items
			.groupBy { item -> item.purchase.doneBy }
			.mapKeys { (buyer, items) ->
				val invoice = Invoice(
					from = range.from, to = range.to,
					payer = user, receiver = buyer, group = group,
					total = valueOf(0),
					finalAmount = valueOf(0)
				)
				val sum = items.sumOf {
					val sum = calculatePricePart(it)
					invoice.addItem(it, sum)
					sum
				}
				invoice.total = sum
				invoice.finalAmount = sum

				invoice
			}
			.keys
			.toList()
		for (invoice in invoices) {
			checkDates(invoice)
		}
		return invoices
	}

	private fun calculatePricePart(item: PurchaseItem): BigDecimal {
		val users = item.usedBy().size
		if (users == 0) return valueOf(0)
		return item.price / users.toBigDecimal()
	}

	private fun validateRequest(range: DateRange) {
		require(range.from.isBefore(range.to) || range.from.isEqual(range.to)) {
			"Invalid date range"
		}
	}

	private fun checkDates(invoice: Invoice) {
		val start = invoice.from
		val end = invoice.to
		val group = invoice.group
		val payer = invoice.payer
		val receiver = invoice.receiver

		checkOverlap("start", start, group, payer, receiver)
		checkOverlap("end", end, group, payer, receiver)
	}

	private fun checkOverlap(
		name: String, date: LocalDate,
		group: UserGroup, payer: User, receiver: User
	) {
		val result = invoiceRepository.existsAnyForUserPairWithDateIn(
			date = date,
			group = group,
			payer = payer,
			receiver = receiver
		)
		require(result.isEmpty()) {
			val invoice = result.first().toString()
			"Invoice $name date range overlaps with $invoice"
		}
	}
}

data class TimeRange(
	val from: LocalDateTime,
	val to: LocalDateTime
)
