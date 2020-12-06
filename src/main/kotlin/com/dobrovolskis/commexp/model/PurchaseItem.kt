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

import com.dobrovolskis.commexp.config.ID_COLUMN_NAME
import com.dobrovolskis.commexp.config.TABLE_PURCHASE_ITEMS
import com.dobrovolskis.commexp.config.TABLE_USERS_USE_PURCHASE_ITEMS
import java.util.UUID
import javax.persistence.CascadeType.ALL
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType.LAZY
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.Transient
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.05
 */
@Entity
@Table(name = TABLE_PURCHASE_ITEMS)
class PurchaseItem(

	@NotEmpty
	@Column(name = "name")
	var name: String,

	@NotNull
	@Column(name = "price")
	var priceCents: Int,

	@NotNull
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "purchase_id")
	var purchase: Purchase,

) : IdEntity() {

	@NotNull
	@Column(name = "description")
	var description: String = ""

	@NotNull
	@Column(name = "used_up")
	var usedUp: Boolean = false

	@ManyToMany(targetEntity = User::class, cascade = [ALL], fetch = LAZY)
	@JoinTable(
		name = TABLE_USERS_USE_PURCHASE_ITEMS,
		joinColumns = [JoinColumn(name = "purchase_item_id", referencedColumnName = ID_COLUMN_NAME)],
		inverseJoinColumns = [JoinColumn(name = "user_id", referencedColumnName = ID_COLUMN_NAME)],
	)
	private val _usedBy: MutableList<User> = mutableListOf()

	@Transient
	fun usedBy() : List<User> = _usedBy.toList()

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
