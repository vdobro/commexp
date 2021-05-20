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

package com.dobrovolskis.commexp.web.controller

import com.dobrovolskis.commexp.config.PATH_USERS
import com.dobrovolskis.commexp.exception.ResourceAccessError
import com.dobrovolskis.commexp.exception.ResourceNotFoundError
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.service.UserService
import com.dobrovolskis.commexp.web.ControllerUtils
import com.dobrovolskis.commexp.web.assembler.UserAssembler
import com.dobrovolskis.commexp.web.dto.PasswordChangeRequest
import com.dobrovolskis.commexp.web.dto.UserDto
import com.dobrovolskis.commexp.web.request.UserCreationRequest
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.validation.Valid

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.06
 */
@RestController
@CrossOrigin
@RequestMapping(value = [PATH_USERS])
class UserController(
	private val controllerUtils: ControllerUtils,
	private val userService: UserService,
	private val assembler: UserAssembler
) {

	@RequestMapping(method = [POST])
	fun create(@RequestBody @Valid request: UserCreationRequest) {
		userService.addUser(
			name = request.name,
			username = request.username,
			password = request.password
		)
	}

	@RequestMapping(method = [GET], path = ["/{userId}"])
	fun getUser(@PathVariable userId: UUID): UserDto {
		if (!userService.anyGroupsShared(controllerUtils.getCurrentUser(), userId)) {
			throw ResourceAccessError("Cannot access user")
		}
		return mapUserToDto(userService.getById(userId))
	}

	@RequestMapping(method = [GET])
	fun getUserInfo(): UserDto {
		return mapUserToDto(controllerUtils.getCurrentUser())
	}

	@RequestMapping(method = [POST], value = ["/password"])
	fun changePassword(@RequestBody request: PasswordChangeRequest) {
		val user = getUserInfo()
		userService.changePassword(
			username = user.username,
			oldPassword = request.oldPassword,
			newPassword = request.newPassword
		)
	}

	private fun mapUserToDto(user: User) = assembler.toDto(user)
}
