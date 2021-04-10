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

package com.dobrovolskis.commexp.model

import com.dobrovolskis.commexp.config.Constraints.Strings.DIGITS_FRACTION
import com.dobrovolskis.commexp.config.Constraints.Strings.DIGITS_INTEGER
import com.dobrovolskis.commexp.config.Table.INVOICE_ITEMS
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.Digits
import javax.validation.constraints.NotNull

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.03.17
 */
@Entity
@Table(name = INVOICE_ITEMS)
class InvoiceItem(

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "invoice_id",
		nullable = false,
		updatable = false
	)
	var invoice: Invoice,

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "item_id",
		nullable = false,
		updatable = false
	)
	var item: PurchaseItem,

	@NotNull
	@Column(
		name = "sum",
		nullable = false,
	)
	@Digits(integer = DIGITS_INTEGER, fraction = DIGITS_FRACTION)
	var sum: BigDecimal,

	) : IdEntity()
