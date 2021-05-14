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

package com.dobrovolskis.commexp.web.usecase.user

import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.service.UserGroupService
import com.dobrovolskis.commexp.web.usecase.BaseRequestHandler
import com.dobrovolskis.commexp.web.usecase.verifyAccessToGroup
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.04.28
 */
@Service
@Transactional(readOnly = true)
class GetUsersInGroup(private val groupService: UserGroupService) : BaseRequestHandler<UUID, List<User>> {
	override operator fun invoke(currentUser: User, request: UUID): List<User> {
		val group = groupService.find(request)
		verifyAccessToGroup(currentUser, group)
		return group.users().toList()
	}
}
