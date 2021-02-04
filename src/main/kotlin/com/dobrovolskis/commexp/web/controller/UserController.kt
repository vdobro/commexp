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
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.service.UserService
import com.dobrovolskis.commexp.web.ControllerUtils
import com.dobrovolskis.commexp.web.dto.UserDto
import com.dobrovolskis.commexp.web.request.UserCreationRequest
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
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
	private val userService: UserService
) {

	@RequestMapping(method = [RequestMethod.POST])
	fun create(@RequestBody @Valid request: UserCreationRequest) {
		userService.addUser(
			name = request.name,
			username = request.username,
			password = request.password
		)
	}

	@RequestMapping(method = [RequestMethod.GET])
	fun getUserInfo(): UserDto {
		return mapUserToDto(controllerUtils.getCurrentUser())
	}

	private fun mapUserToDto(user: User): UserDto {
		return UserDto(
			id = user.id()!!,
			username = user.username,
			name = user.name
		)
	}
}
