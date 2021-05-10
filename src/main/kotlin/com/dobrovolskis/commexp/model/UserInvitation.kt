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

package com.dobrovolskis.commexp.model

import com.dobrovolskis.commexp.config.Table.USER_GROUP_INVITATIONS
import java.time.ZonedDateTime
import java.time.ZonedDateTime.now
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.06
 */
@Entity
@Table(name = USER_GROUP_INVITATIONS)
class UserInvitation(

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "creator_id",
		nullable = false,
		updatable = false
	)
	var creator: User,

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "group_id",
		nullable = false,
		updatable = false
	)
	var group: UserGroup,
) : IdEntity() {

	@NotNull
	@Column(
		name = "created",
		updatable = false,
		nullable = false
	)
	var created: ZonedDateTime = now()

	@Column(name = "accepted")
	var accepted: ZonedDateTime? = null
}
