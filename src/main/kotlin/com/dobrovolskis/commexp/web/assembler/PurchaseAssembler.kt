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

package com.dobrovolskis.commexp.web.assembler

import com.dobrovolskis.commexp.model.Purchase
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.web.dto.PurchaseDto
import com.dobrovolskis.commexp.web.usecase.purchase.GetTotalSum
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.03.23
 */
@Service
@Transactional(readOnly = true)
class PurchaseAssembler(private val getSum: GetTotalSum) {
	fun toDto(user: User, purchase: Purchase) = PurchaseDto(
		id = purchase.id()!!,
		shopId = purchase.shop.id()!!,
		time = purchase.shoppingTime,
		creation = purchase.created,
		doneBy = purchase.doneBy.id()!!,
		sum = getSum(currentUser = user, request = purchase.id()!!)
	)
}
