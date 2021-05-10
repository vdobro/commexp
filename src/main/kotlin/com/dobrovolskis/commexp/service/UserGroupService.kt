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

package com.dobrovolskis.commexp.service

import com.dobrovolskis.commexp.exception.ResourceNotFoundError
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.model.UserGroup
import com.dobrovolskis.commexp.model.UserInvitation
import com.dobrovolskis.commexp.repository.UserGroupRepository
import com.dobrovolskis.commexp.repository.UserInvitationRepository
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
class UserGroupService(
	private val repository: UserGroupRepository,
	private val invitationRepository: UserInvitationRepository,
) {

	fun find(id: UUID): UserGroup =
		repository.findByIdOrNull(id) ?: throw ResourceNotFoundError("User group $id not found")


	fun createGroup(name: String): UserGroup = repository.save(
		UserGroup(
			name = name
		)
	)

	fun addUser(group: UserGroup, user: User): UserGroup {
		require(repository.existsById(group.id()!!)) {
			"Group does not exist"
		}
		require (!group.users().contains(user)) {
			"User already in group"
		}
		group.addUser(user)
		return repository.save(group)
	}

	fun createInvitation(
		group: UserGroup,
		invitedBy: User,
	): UserInvitation {
		require(invitedBy.isInGroup(group)) {
			"User cannot invite to a group they are not themselves a part of"
		}
		return invitationRepository.save(
			UserInvitation(
				creator = invitedBy,
				group = group
			)
		)
	}

	fun acceptInvitation(user: User,
	                     invitation: UserInvitation): UserGroup {
		val group = invitation.group
		invitation.accepted = ZonedDateTime.now()
		val resultingGroup = addUser(group, user)
		invitationRepository.save(invitation)
		return resultingGroup
	}
}
