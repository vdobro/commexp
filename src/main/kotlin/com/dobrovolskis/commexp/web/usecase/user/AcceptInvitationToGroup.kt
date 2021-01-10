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

package com.dobrovolskis.commexp.web.usecase.user

import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.model.UserGroup
import com.dobrovolskis.commexp.repository.UserInvitationRepository
import com.dobrovolskis.commexp.service.UserGroupService
import com.dobrovolskis.commexp.web.usecase.BaseRequestHandler
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime
import java.util.UUID

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.06
 */
@Service
@Transactional
class AcceptInvitationToGroup(
	private val invitationRepository: UserInvitationRepository,
	private val userGroupService: UserGroupService
) : BaseRequestHandler<UUID, UserGroup> {

	override fun invoke(currentUser: User, request: UUID): UserGroup {
		val invitation = invitationRepository.findByIdOrNull(request)
			?: throw Error("Invitation $request not found")
		require(invitation.target == currentUser) {
			"User denied access to invitation"
		}
		require(invitation.accepted == null) {
			"Invitation already accepted"
		}
		val group = invitation.group
		invitation.accepted = ZonedDateTime.now()
		val resultingGroup = userGroupService.addUser(group, currentUser)
		invitationRepository.save(invitation)
		return resultingGroup
	}
}
