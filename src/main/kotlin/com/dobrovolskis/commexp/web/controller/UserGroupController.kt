/*
 * Copyright (C) 2021 Vitalijus Dobrovolskis
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

import com.dobrovolskis.commexp.config.PATH_USER_GROUPS
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.model.UserGroup
import com.dobrovolskis.commexp.model.UserInvitation
import com.dobrovolskis.commexp.web.ControllerUtils
import com.dobrovolskis.commexp.web.assembler.InvitationAssembler
import com.dobrovolskis.commexp.web.assembler.UserAssembler
import com.dobrovolskis.commexp.web.assembler.UserGroupAssembler
import com.dobrovolskis.commexp.web.dto.UserDto
import com.dobrovolskis.commexp.web.dto.UserGroupDto
import com.dobrovolskis.commexp.web.dto.UserInvitationDto
import com.dobrovolskis.commexp.web.request.GroupCreationRequest
import com.dobrovolskis.commexp.web.request.GroupInvitationRequest
import com.dobrovolskis.commexp.web.usecase.user.CreateGroup
import com.dobrovolskis.commexp.web.usecase.user.CreateInvitationToGroup
import com.dobrovolskis.commexp.web.usecase.user.FindGroup
import com.dobrovolskis.commexp.web.usecase.user.GetDefaultGroup
import com.dobrovolskis.commexp.web.usecase.user.GetUserGroupList
import com.dobrovolskis.commexp.web.usecase.user.GetUsersInGroup
import com.dobrovolskis.commexp.web.usecase.user.JoinGroupWithInvitation
import com.dobrovolskis.commexp.web.usecase.user.SetDefaultGroup
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RequestMethod.PUT
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.validation.Valid

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.06
 */
@RestController
@RequestMapping(value = [PATH_USER_GROUPS])
class UserGroupController(
	private val createGroup: CreateGroup,
	private val createInvitationToGroup: CreateInvitationToGroup,
	private val joinGroupWithInvitation: JoinGroupWithInvitation,
	private val getUserGroupList: GetUserGroupList,
	private val findGroup: FindGroup,
	private val getDefaultGroup: GetDefaultGroup,
	private val setDefaultGroup: SetDefaultGroup,
	private val getUsersInGroup: GetUsersInGroup,
	private val controllerUtils: ControllerUtils,
	private val groupAssembler: UserGroupAssembler,
	private val invitationAssembler: InvitationAssembler,
	private val userAssembler: UserAssembler,
) {

	@RequestMapping(method = [POST])
	fun createNew(@RequestBody creationRequest: GroupCreationRequest): UserGroupDto =
		mapToDto(createGroup(getUser(), creationRequest))

	@RequestMapping(method = [GET])
	fun getMyGroups(): List<UserGroupDto> =
		getUserGroupList(getUser()).map(this::mapToDto)

	@RequestMapping(method = [GET], path = ["/{id}"])
	fun get(@PathVariable id: UUID): UserGroupDto {
		return mapToDto(findGroup(getUser(), id))
	}

	@RequestMapping(method = [GET], path = ["/{id}/users"])
	fun getUsers(@PathVariable id: UUID): List<UserDto> {
		return getUsersInGroup(getUser(), id).map { mapToDto(it) }
	}

	@RequestMapping(method = [GET], path = ["/default"])
	fun getDefault(): UserGroupDto? {
		val group = getDefaultGroup(getUser())
		return group?.let { mapToDto(it) }
	}

	@RequestMapping(method = [PUT], path = ["/default/{id}"])
	fun setDefault(@PathVariable id: UUID) = setDefaultGroup(getUser(), id)

	@RequestMapping(
		method = [POST],
		path = ["/invite"]
	)
	fun createInvitation(@RequestBody @Valid request: GroupInvitationRequest): UserInvitationDto =
		mapToDto(createInvitationToGroup(getUser(), request))

	@RequestMapping(
		method = [POST],
		path = ["/join/{invitationId}"]
	)
	fun acceptInvitation(@PathVariable invitationId: UUID): UserGroupDto =
		mapToDto(joinGroupWithInvitation(getUser(), invitationId))

	private fun mapToDto(userGroup: UserGroup) = groupAssembler.toDto(userGroup)

	private fun mapToDto(invitation: UserInvitation) = invitationAssembler.toDto(invitation)

	private fun mapToDto(user: User) = userAssembler.toDto(user)

	private fun getUser(): User = controllerUtils.getCurrentUser()
}

