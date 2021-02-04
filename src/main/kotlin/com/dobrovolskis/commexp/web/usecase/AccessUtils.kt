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

package com.dobrovolskis.commexp.web.usecase

import com.dobrovolskis.commexp.exception.ResourceAccessError
import com.dobrovolskis.commexp.model.Purchase
import com.dobrovolskis.commexp.model.PurchaseItem
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.model.UserGroup

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.02.04
 */
fun verifyAccessToGroup(user: User, group: UserGroup) {
	if (!user.isInGroup(group)) {
		throw ResourceAccessError("User not in group")
	}
}

fun verifyAccessToItem(user: User, purchaseItem: PurchaseItem) {
	if (!user.isInGroup(purchaseItem.purchase.group)) {
		throw ResourceAccessError("Access denied to purchase item")
	}
}

fun verifyAccessToPurchase(purchase: Purchase, user: User) {
	if (!user.isInGroup(purchase.group)) {
		throw ResourceAccessError("Access denied to purchase")
	}
}
