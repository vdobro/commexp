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

import com.dobrovolskis.commexp.model.Purchase
import com.dobrovolskis.commexp.model.PurchaseItem
import com.dobrovolskis.commexp.model.Shop
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.model.UserGroup
import com.dobrovolskis.commexp.repository.PurchaseItemRepository
import com.dobrovolskis.commexp.repository.PurchaseRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime
import java.util.UUID

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.05
 */
@Service
@Transactional
class PurchaseService(
	private val repository: PurchaseRepository,
	private val itemRepository: PurchaseItemRepository
) {
	fun find(id: UUID): Purchase {
		return repository.findByIdOrNull(id)
			?: throw Error("Purchase list $id not found")
	}

	fun createNew(
		creator: User,
		user: User,
		group: UserGroup,
		shop: Shop,
		shoppingTime: ZonedDateTime,
	): Purchase = repository.save(
		Purchase(
			doneBy = user,
			group = group,
			shop = shop,
			shoppingTime = shoppingTime,
			createdBy = creator,
		)
	)

	fun addItem(purchase: Purchase, itemName: String, price: Int): PurchaseItem =
		itemRepository.save(
			PurchaseItem(
				name = itemName,
				priceCents = price,
				purchase = find(purchase.id()!!),
			)
		)

	fun removeItem(purchaseItem: PurchaseItem) =
		itemRepository.delete(purchaseItem)
}