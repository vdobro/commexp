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

import com.dobrovolskis.commexp.config.Constraints.Strings.LENGTH_SHORT
import com.dobrovolskis.commexp.config.Table.USER_GROUPS
import com.dobrovolskis.commexp.config.Table.USER_GROUPS_USERS
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.Table
import javax.persistence.Transient
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.06
 */
@Entity
@Table(name = USER_GROUPS)
class UserGroup(

	@NotEmpty
	@Column(
		name = "name",
		nullable = false
	)
	@Size(max = LENGTH_SHORT)
	var name: String = ""

) : IdEntity() {

	@ManyToMany(targetEntity = User::class, fetch = FetchType.LAZY)
	@JoinTable(
		name = USER_GROUPS_USERS,
		joinColumns = [JoinColumn(name = "user_group_id")],
		inverseJoinColumns = [JoinColumn(name = "user_id")],
	)
	private val _users: MutableList<User> = mutableListOf()

	@Transient
	fun users(): List<User> = this._users.toList()

	@Transient
	fun containsUser(user: User): Boolean {
		return _users.contains(user)
	}

	fun addUser(user: User) {
		if (containsUser(user)) {
			return
		}
		_users.add(user)
	}

	fun removeUser(user: User) {
		if (containsUser(user)) {
			return
		}
		_users.remove(user)
	}
}
