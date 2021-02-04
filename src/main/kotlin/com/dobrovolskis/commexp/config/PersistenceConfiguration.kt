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

package com.dobrovolskis.commexp.config

import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.05
 */
@Configuration
@EnableTransactionManagement
class PersistenceConfiguration

object Table {
	private const val PREFIX = "cx_"

	const val USERS = "${PREFIX}user"
	const val USER_GROUPS = "${PREFIX}user_group"
	const val USER_GROUPS_USERS = "${PREFIX}user_group__user"
	const val USER_GROUP_INVITATIONS = "${PREFIX}user_group_invitation"
	const val PURCHASES = "${PREFIX}purchase"
	const val PURCHASE_ITEMS = "${PREFIX}purchase_item"
	const val USERS_USE_PURCHASE_ITEMS = "${PREFIX}purchase_item__user"
	const val SHOPS = "${PREFIX}shop"
	const val INVOICES = "${PREFIX}invoice"
	const val PAYMENTS = "${PREFIX}payment"
}

const val ID_COLUMN_NAME = "id"

object Constraints {
	object Strings {
		const val LENGTH_LONG = 1000
		const val LENGTH_MEDIUM = 5000
		const val LENGTH_SHORT = 200
		const val LENGTH_MIN = 20
		const val DIGITS_INTEGER = 10
		const val DIGITS_FRACTION = 2
	}
}
