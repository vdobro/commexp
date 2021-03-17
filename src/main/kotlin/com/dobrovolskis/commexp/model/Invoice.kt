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

package com.dobrovolskis.commexp.model

import com.dobrovolskis.commexp.config.Constraints.Strings.DIGITS_FRACTION
import com.dobrovolskis.commexp.config.Constraints.Strings.DIGITS_INTEGER
import com.dobrovolskis.commexp.config.Table.INVOICES
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE
import javax.persistence.CascadeType.ALL
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType.LAZY
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.Transient
import javax.validation.constraints.Digits
import javax.validation.constraints.NotNull

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.05
 */
@Entity
@Table(name = INVOICES)
class Invoice(

	@NotNull
	@Column(
		name = "period_start",
		nullable = false,
		updatable = false
	)
	var from: LocalDate,

	@NotNull
	@Column(
		name = "period_end",
		nullable = false,
		updatable = false
	)
	var to: LocalDate,

	@NotNull
	@ManyToOne(fetch = LAZY)
	@JoinColumn(
		name = "payer_id",
		nullable = false,
		updatable = false
	)
	var payer: User,

	@NotNull
	@ManyToOne(fetch = LAZY)
	@JoinColumn(
		name = "receiver_id",
		nullable = false,
		updatable = false
	)
	var receiver: User,

	@NotNull
	@ManyToOne(fetch = LAZY)
	@JoinColumn(
		name = "group_id",
		nullable = false,
		updatable = false
	)
	var group: UserGroup,

	@NotNull
	@Column(
		name = "total",
		nullable = false,
	)
	@Digits(integer = DIGITS_INTEGER, fraction = DIGITS_FRACTION)
	var total: BigDecimal,

	@NotNull
	@Column(
		name = "final",
		nullable = false
	)
	@Digits(integer = DIGITS_INTEGER, fraction = DIGITS_FRACTION)
	var finalAmount: BigDecimal,

	) : IdEntity() {

	@Column(name = "reduction")
	@Digits(integer = DIGITS_INTEGER, fraction = DIGITS_FRACTION)
	var reduction: BigDecimal? = null

	@OneToOne(fetch = LAZY, cascade = [ALL])
	@JoinColumn(name = "mirror_invoice_id")
	var mirror: Invoice? = null

	@OneToMany(
		targetEntity = InvoiceItem::class,
		fetch = LAZY,
		mappedBy = "invoice",
		orphanRemoval = true,
		cascade = [ALL]
	)
	private val _items: MutableList<InvoiceItem> = mutableListOf()

	@Transient
	fun items() = _items.toList()

	fun addItem(item: PurchaseItem, sum : BigDecimal) {
		_items.add(
			InvoiceItem(this, item = item, sum = sum)
		)
	}

	override fun toString(): String {
		return "${payer.name}'s invoice for ${from.format(OUTPUT_FORMATTER)} - ${to.format(OUTPUT_FORMATTER)}"
	}
}

private val OUTPUT_FORMATTER = ISO_LOCAL_DATE
