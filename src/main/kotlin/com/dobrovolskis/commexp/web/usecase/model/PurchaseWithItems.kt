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

package com.dobrovolskis.commexp.web.usecase.model

import com.dobrovolskis.commexp.model.PurchaseItem
import com.dobrovolskis.commexp.model.Shop
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.model.UserGroup
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.05.21
 */
data class PurchaseWithItems(
	val id: UUID,
	var shop: Shop,
	var doneBy: User,
	var createdBy: User,
	var group: UserGroup,
	val shoppingTime: LocalDate,
	var created: LocalDateTime,
	val items: List<PurchaseItem>
)
