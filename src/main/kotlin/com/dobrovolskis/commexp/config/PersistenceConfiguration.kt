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

const val TABLE_PREFIX = "cx_"

const val TABLE_USERS = "${TABLE_PREFIX}user"
const val TABLE_USER_GROUPS = "${TABLE_PREFIX}user_group"
const val TABLE_USER_GROUPS_USERS = "${TABLE_PREFIX}user_group__user"
const val TABLE_USER_GROUP_INVITATIONS = "${TABLE_PREFIX}user_group_invitation"
const val TABLE_PURCHASES = "${TABLE_PREFIX}purchase"
const val TABLE_PURCHASE_ITEMS = "${TABLE_PREFIX}purchase_item"
const val TABLE_USERS_USE_PURCHASE_ITEMS = "${TABLE_PREFIX}purchase_item__user"
const val TABLE_SHOPS = "${TABLE_PREFIX}shop"
const val TABLE_INVOICES = "${TABLE_PREFIX}invoice"

const val ID_COLUMN_NAME = "id"