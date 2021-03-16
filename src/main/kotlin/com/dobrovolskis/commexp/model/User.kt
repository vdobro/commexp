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
import com.dobrovolskis.commexp.config.Table.USERS
import com.dobrovolskis.commexp.config.Table.USERS_USE_PURCHASE_ITEMS
import com.dobrovolskis.commexp.config.Table.USER_GROUPS_USERS
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.CascadeType.ALL
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType.LAZY
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.persistence.Transient
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.05
 */
@Entity
@Table(name = USERS)
class User(

	@NotEmpty
	@Column(name = "name", nullable = false)
	@Size(min = 0, max = LENGTH_SHORT)
	var name: String,

	@NotEmpty
	@Column(name = "username", nullable = false, unique = true)
	@Size(min = 0, max = LENGTH_SHORT)
	private var username: String,

	@NotEmpty
	@Column(name = "password", nullable = false)
	@Size(max = LENGTH_SHORT)
	private var password: String,

	) : IdEntity(), UserDetails {

	@NotNull
	@Column(name = "non_expired", nullable = false)
	var accountNonExpired: Boolean = true

	@NotNull
	@Column(name = "non_locked", nullable = false)
	var accountNonLocked: Boolean = true

	@NotNull
	@Column(name = "credentials_not_expired", nullable = false)
	var credentialsNonExpired: Boolean = true

	@NotNull
	@Column(name = "enabled", nullable = false)
	var enabled: Boolean = true

	@OneToMany(
		targetEntity = Purchase::class,
		fetch = LAZY,
		mappedBy = "doneBy",
		orphanRemoval = true,
		cascade = [ALL]
	)
	private val _donePurchases: List<Purchase> = mutableListOf()

	@OneToMany(
		targetEntity = Purchase::class,
		fetch = LAZY,
		mappedBy = "doneBy",
		orphanRemoval = true,
		cascade = [ALL]
	)
	private val _createdPurchases: List<Purchase> = mutableListOf()

	@ManyToMany(targetEntity = PurchaseItem::class, fetch = LAZY)
	@JoinTable(
		name = USERS_USE_PURCHASE_ITEMS,
		joinColumns = [JoinColumn(name = "user_id")],
		inverseJoinColumns = [JoinColumn(name = "purchase_item_id")],
	)
	private val _usedPurchaseItems: List<PurchaseItem> = mutableListOf()

	@ManyToMany(targetEntity = UserGroup::class, fetch = LAZY)
	@JoinTable(
		name = USER_GROUPS_USERS,
		joinColumns = [JoinColumn(name = "user_id")],
		inverseJoinColumns = [JoinColumn(name = "user_group_id")],
	)
	val userGroups: List<UserGroup> = mutableListOf()

	override fun getUsername(): String = username
	override fun getPassword(): String = password
	override fun isAccountNonExpired(): Boolean = accountNonExpired
	override fun isAccountNonLocked(): Boolean = accountNonLocked
	override fun isCredentialsNonExpired(): Boolean = credentialsNonExpired
	override fun isEnabled(): Boolean = enabled

	override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
		mutableSetOf(SimpleGrantedAuthority("user"))

	@Transient
	fun isInGroup(group: UserGroup): Boolean {
		return group.containsUser(this)
	}

	override fun toString(): String {
		return "User[$username]"
	}
}
