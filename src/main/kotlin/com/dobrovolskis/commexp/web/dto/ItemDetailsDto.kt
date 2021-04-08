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

package com.dobrovolskis.commexp.web.dto

import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.03.23
 */
data class ItemDetailsDto(
	val id: UUID,
	val usedUp: Boolean,
	val name: String,
	val description: String,
	val shopName: String,
	val shoppingTime: LocalDate,
	val purchaseId: UUID,
	val paidBy: UserDto,
	val price: BigDecimal
)
