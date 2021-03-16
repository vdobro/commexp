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
import com.dobrovolskis.commexp.config.Constraints.Strings.LENGTH_MEDIUM
import com.dobrovolskis.commexp.config.Constraints.Strings.LENGTH_SHORT
import com.dobrovolskis.commexp.config.ID_COLUMN_NAME
import com.dobrovolskis.commexp.config.Table.PURCHASE_ITEMS
import com.dobrovolskis.commexp.config.Table.USERS_USE_PURCHASE_ITEMS
import java.math.BigDecimal
import javax.persistence.CascadeType.ALL
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType.LAZY
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.Transient
import javax.validation.constraints.Digits
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.05
 */
@Entity
@Table(name = PURCHASE_ITEMS)
class PurchaseItem(

	@NotEmpty
	@Column(name = "name", nullable = false)
	@Size(max = LENGTH_SHORT)
	var name: String,

	@NotNull
	@Column(name = "price", nullable = false)
	@Digits(integer = DIGITS_INTEGER, fraction = DIGITS_FRACTION)
	var price: BigDecimal,

	@NotNull
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "purchase_id", nullable = false)
	var purchase: Purchase,

	@NotNull
	@Column(name = "description", nullable = false)
	@Size(max = LENGTH_MEDIUM)
	var description: String = "",

	) : IdEntity() {

	@NotNull
	@Column(name = "used_up", nullable = false)
	var usedUp: Boolean = false

	@ManyToMany(targetEntity = User::class, cascade = [ALL], fetch = LAZY)
	@JoinTable(
		name = USERS_USE_PURCHASE_ITEMS,
		joinColumns = [JoinColumn(
			name = "purchase_item_id",
			referencedColumnName = ID_COLUMN_NAME,
			nullable = false,
			updatable = false
		)],
		inverseJoinColumns = [JoinColumn(
			name = "user_id",
			referencedColumnName = ID_COLUMN_NAME,
			nullable = false,
			updatable = false
		)],
	)
	private val _usedBy: MutableList<User> = mutableListOf()

	@Transient
	fun usedBy(): List<User> = _usedBy.toList()

	fun addUser(user: User) {
		if (_usedBy.contains(user)) {
			return
		}
		_usedBy.add(user)
	}

	fun removeUser(user: User) {
		_usedBy.remove(user)
	}
}
