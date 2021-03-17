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

package com.dobrovolskis.commexp.web.usecase.purchase

import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.service.PurchaseService
import com.dobrovolskis.commexp.web.usecase.BaseRequestHandler
import com.dobrovolskis.commexp.web.usecase.verifyAccessToPurchase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.UUID

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.03.17
 */
@Service
@Transactional(readOnly = true)
class GetTotalSum(private val purchaseService: PurchaseService) : BaseRequestHandler<UUID, BigDecimal> {

	override operator fun invoke(currentUser: User, request: UUID): BigDecimal {
		val purchase = purchaseService.find(request)
		verifyAccessToPurchase(purchase = purchase, user = currentUser)
		return purchase.items().sumOf { it.price }
	}
}
