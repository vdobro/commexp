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

package com.dobrovolskis.commexp.service

import com.dobrovolskis.commexp.exception.ResourceNotFoundError
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.model.UserGroup
import com.dobrovolskis.commexp.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.05
 */
@Service
@Transactional
class UserService(
	private val repository: UserRepository,
	private val passwordEncoder: PasswordEncoder,
) {

	fun addUser(
		name: String,
		username: String,
		password: String
	): User {
		require(!repository.existsByUsername(username)) {
			"Username already taken"
		}
		return repository.save(
			User(
				name = name,
				username = username,
				password = passwordEncoder.encode(password)
			)
		)
	}

	fun getAllGroups(id: UUID): List<UserGroup> {
		val user = getById(id)
		val result = user.userGroups
		return result.toList()
	}

	fun getById(id: UUID): User {
		return repository.findByIdOrNull(id)
			?: throw ResourceNotFoundError("User not found")
	}

	fun findByUsername(username: String): User {
		return repository.findByUsername(username)
			?: throw ResourceNotFoundError("User $username not found")
	}

	fun setDefaultGroup(userId: UUID, group: UserGroup) {
		val user = getById(userId)
		user.defaultGroup = group
		repository.save(user)
	}

	fun anyGroupsShared(current: User, userId: UUID) : Boolean {
		val groups = getById(current.id()!!).userGroups.toSet()
		val other = getById(userId)
		return other.userGroups.toSet().intersect(groups).isNotEmpty()
	}

	fun changePassword(username: String,
	                   oldPassword: String,
	                   newPassword: String) {
		val user = findByUsername(username)
		require (hasPassword(user, oldPassword)) {
			"Wrong old password"
		}
		resetPassword(user, newPassword)
	}

	private fun resetPassword(user: User, password: String) {
		require(password.length >= 8) {
			"Password too short"
		}
		require(!hasPassword(user, password)) {
			"New password has to be different"
		}
		user.password = passwordEncoder.encode(password)
		repository.save(user)
	}

	private fun hasPassword(user: User, password: String) : Boolean {
		return passwordEncoder.matches(password, user.password)
	}
}
