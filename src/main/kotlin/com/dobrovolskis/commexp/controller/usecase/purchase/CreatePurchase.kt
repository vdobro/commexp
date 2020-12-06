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

package com.dobrovolskis.commexp.controller.usecase.purchase

import com.dobrovolskis.commexp.controller.getCurrentUser
import com.dobrovolskis.commexp.controller.request.PurchaseCreationRequest
import com.dobrovolskis.commexp.controller.usecase.BaseRequestHandler
import com.dobrovolskis.commexp.model.Purchase
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.repository.ShopRepository
import com.dobrovolskis.commexp.service.PurchaseService
import com.dobrovolskis.commexp.service.UserGroupService
import com.dobrovolskis.commexp.service.UserService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.06
 */
@Service
@Transactional
class CreatePurchase(
	private val userService: UserService,
	private val shopRepository: ShopRepository,
	private val groupService: UserGroupService,
	private val purchaseService: PurchaseService,
) : BaseRequestHandler<PurchaseCreationRequest, Purchase> {

	override operator fun invoke(currentUser: User, request: PurchaseCreationRequest): Purchase {
		val user = userService.getById(request.doneBy)
		val shop = shopRepository.findByIdOrNull(request.shop)
			?: throw Error("Shop ${request.shop} not found")
		val group = groupService.find(request.groupId)
		return purchaseService.createNew(
			user = user,
			shop = shop,
			shoppingTime = request.shoppingTime,
			group = group,
			creator = getCurrentUser(),
		)
	}

	private fun validateRequest(user: User, request: PurchaseCreationRequest) {
		val group = groupService.find(request.groupId)
		require(user.isInGroup(group)) {
			"User does not belong to group ${request.groupId}"
		}
	}
}