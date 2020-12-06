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

import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.model.UserGroup
import com.dobrovolskis.commexp.model.UserInvitation
import com.dobrovolskis.commexp.repository.UserGroupRepository
import com.dobrovolskis.commexp.repository.UserInvitationRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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

	fun find(id: UUID): UserGroup {
		return repository.findByIdOrNull(id) ?: throw Error("User group $id not found")
	}

	fun createGroup(name: String): UserGroup {
		return repository.save(
			UserGroup(
				name = name
			)
		)
	}

	fun addUser(group: UserGroup, user: User): UserGroup {
		require(repository.existsById(group.id()!!)) {
			"Group does not exist"
		}
		if (group.users().contains(user)) {
			return group;
		}
		group.addUser(user)
		return repository.save(group)
	}

	fun inviteUser(
		group: UserGroup,
		invitedBy: User,
		userToInvite: User
	): UserInvitation {
		require(!userToInvite.isInGroup(group)) {
			"User is already in the group"
		}
		require(invitedBy.isInGroup(group)) {
			"User cannot invite to a group they are not a part of"
		}
		require(invitedBy != userToInvite) {
			"User cannot invite themselves"
		}
		return invitationRepository.save(
			UserInvitation(
				creator = invitedBy,
				target = userToInvite,
				group = group
			)
		)
	}
}