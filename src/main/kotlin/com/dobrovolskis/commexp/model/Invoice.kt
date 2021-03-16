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

import com.dobrovolskis.commexp.config.ColumnType.MOMENT
import com.dobrovolskis.commexp.config.Table.INVOICES
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType.LAZY
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
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
		columnDefinition = MOMENT,
		nullable = false,
		updatable = false
	)
	var from: LocalDateTime,

	@NotNull
	@Column(
		name = "period_end",
		columnDefinition = MOMENT,
		nullable = false,
		updatable = false
	)
	var to: LocalDateTime,

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
		name = "sum",
		nullable = false
	)
	@Digits(integer = 10, fraction = 2)
	var sum: BigDecimal,

	) : IdEntity() {
	override fun toString(): String {
		return "${payer.name}'s invoice for ${from.format(OUTPUT_FORMATTER)} - ${to.format(OUTPUT_FORMATTER)}"
	}
}

private val OUTPUT_FORMATTER = ISO_LOCAL_DATE
