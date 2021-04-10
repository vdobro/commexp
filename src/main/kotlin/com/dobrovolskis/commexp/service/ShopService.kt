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

import com.dobrovolskis.commexp.model.Shop
import com.dobrovolskis.commexp.model.UserGroup
import com.dobrovolskis.commexp.repository.ShopRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.09
 */
@Service
@Transactional
class ShopService(private val repository: ShopRepository) {

	fun createNew(
		group: UserGroup,
		name: String
	): Shop = repository.save(
		Shop(
			name = name,
			group = group
		)
	)

	fun getAllForGroup(group: UserGroup): List<Shop> =
		repository.getAllByGroup(group)

	fun findOrCreate(name: String, group: UserGroup): Shop =
		repository.findByGroupAndName(userGroup = group, name = name)
			?: createNew(group = group, name = name)
}
