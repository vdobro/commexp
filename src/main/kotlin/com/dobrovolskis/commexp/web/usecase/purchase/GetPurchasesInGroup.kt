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

package com.dobrovolskis.commexp.web.usecase.purchase

import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.service.PurchaseService
import com.dobrovolskis.commexp.service.UserGroupService
import com.dobrovolskis.commexp.web.usecase.BaseRequestHandler
import com.dobrovolskis.commexp.web.usecase.model.PurchaseWithItems
import com.dobrovolskis.commexp.web.usecase.verifyAccessToGroup
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.09
 */
@Service
@Transactional(readOnly = true)
class GetPurchasesInGroup(
	private val groupService: UserGroupService,
	private val purchaseService: PurchaseService
) : BaseRequestHandler<UUID, List<PurchaseWithItems>> {
	override operator fun invoke(currentUser: User, request: UUID): List<PurchaseWithItems> {
		val group = groupService.find(request)
		verifyAccessToGroup(user = currentUser, group = group)

		val all = purchaseService.getAllForGroup(group)

		return all.map { PurchaseWithItems(
			id = it.id()!!,
			shop = it.shop,
			doneBy = it.doneBy,
			createdBy = it.createdBy,
			group = it.group,
			shoppingTime =  it.shoppingTime,
			created = it.created,
			items = it.items()
		) }
	}
}
