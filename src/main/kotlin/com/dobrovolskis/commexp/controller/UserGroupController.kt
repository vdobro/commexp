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

package com.dobrovolskis.commexp.controller

import com.dobrovolskis.commexp.controller.dto.UserGroupDto
import com.dobrovolskis.commexp.controller.dto.UserInvitationDto
import com.dobrovolskis.commexp.controller.request.GroupCreationRequest
import com.dobrovolskis.commexp.controller.request.GroupUserRequest
import com.dobrovolskis.commexp.controller.usecase.user.AcceptInvitationToGroup
import com.dobrovolskis.commexp.controller.usecase.user.InviteUserToGroup
import com.dobrovolskis.commexp.controller.usecase.user.CreateGroup
import com.dobrovolskis.commexp.model.UserGroup
import com.dobrovolskis.commexp.model.UserInvitation
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.06
 */
@RestController
@RequestMapping("/usergroups")
class UserGroupController(
	private val createGroup: CreateGroup,
	private val inviteUser: InviteUserToGroup,
	private val acceptInvitationToGroup: AcceptInvitationToGroup,
) {

	@RequestMapping(method = [POST])
	fun createNew(@RequestBody creationRequest: GroupCreationRequest): UserGroupDto =
		mapToDto(createGroup(getCurrentUser(), creationRequest))

	@RequestMapping(
		method = [POST],
		path = ["/invite"]
	)
	fun inviteUserToGroup(@RequestBody request: GroupUserRequest): UserInvitationDto =
		mapToDto(inviteUser(getCurrentUser(), request))

	@RequestMapping(
		method = [POST],
		path = ["/acceptInvitation"]
	)
	fun acceptInvitation(@RequestParam(required = true) id: UUID): UserGroupDto {
		return mapToDto(acceptInvitationToGroup(getCurrentUser(), id))
	}

	private fun mapToDto(userGroup: UserGroup): UserGroupDto {
		return UserGroupDto(
			id = userGroup.id()!!,
			name = userGroup.name
		)
	}

	private fun mapToDto(invitation: UserInvitation) : UserInvitationDto {
		return UserInvitationDto(
			code = invitation.id()!!.toString(),
			invitedUser = invitation.target.id()!!,
			groupName = invitation.group.name
		)
	}
}

