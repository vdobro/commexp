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

import com.dobrovolskis.commexp.config.PATH_USER_GROUPS
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.model.UserGroup
import com.dobrovolskis.commexp.model.UserInvitation
import com.dobrovolskis.commexp.web.ControllerUtils
import com.dobrovolskis.commexp.web.dto.UserGroupDto
import com.dobrovolskis.commexp.web.dto.UserInvitationDto
import com.dobrovolskis.commexp.web.request.GroupCreationRequest
import com.dobrovolskis.commexp.web.request.GroupUserRequest
import com.dobrovolskis.commexp.web.usecase.user.AcceptInvitationToGroup
import com.dobrovolskis.commexp.web.usecase.user.CreateGroup
import com.dobrovolskis.commexp.web.usecase.user.GetUserGroupList
import com.dobrovolskis.commexp.web.usecase.user.InviteUserToGroup
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.06
 */
@RestController
@RequestMapping(value = [PATH_USER_GROUPS])
class UserGroupController(
	private val createGroup: CreateGroup,
	private val inviteUser: InviteUserToGroup,
	private val acceptInvitationToGroup: AcceptInvitationToGroup,
	private val getUserGroupList: GetUserGroupList,
	private val controllerUtils: ControllerUtils,
) {

	@RequestMapping(method = [POST])
	fun createNew(@RequestBody creationRequest: GroupCreationRequest): UserGroupDto =
		mapToDto(createGroup(getUser(), creationRequest))

	@RequestMapping(method = [GET])
	fun getMyGroups(): List<UserGroupDto> =
		getUserGroupList(getUser()).map(this::mapToDto)

	@RequestMapping(
		method = [POST],
		path = ["/invite"]
	)
	fun inviteUserToGroup(@RequestBody request: GroupUserRequest): UserInvitationDto =
		mapToDto(inviteUser(getUser(), request))

	@RequestMapping(
		method = [POST],
		path = ["/acceptInvitation/{id}"]
	)
	fun acceptInvitation(@PathVariable id: UUID): UserGroupDto =
		mapToDto(acceptInvitationToGroup(getUser(), id))

	private fun mapToDto(userGroup: UserGroup): UserGroupDto {
		return UserGroupDto(
			id = userGroup.id()!!,
			name = userGroup.name
		)
	}

	private fun mapToDto(invitation: UserInvitation): UserInvitationDto {
		return UserInvitationDto(
			code = invitation.id()!!.toString(),
			invitedUser = invitation.target.id()!!,
			groupName = invitation.group.name
		)
	}

	private fun getUser(): User = controllerUtils.getCurrentUser()
}

