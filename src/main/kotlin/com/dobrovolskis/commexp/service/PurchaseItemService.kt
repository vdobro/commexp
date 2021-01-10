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

import com.dobrovolskis.commexp.model.PurchaseItem
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.repository.PurchaseItemRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.05
 */
@Service
@Transactional
class PurchaseItemService(private val repository: PurchaseItemRepository) {

	fun find(id: UUID): PurchaseItem {
		return repository.findByIdOrNull(id) ?: throw Error("Purchase item $id not found")
	}

	fun markAsUsedUp(item: PurchaseItem): PurchaseItem {
		require(!item.usedUp) {
			"Item already used up"
		}
		item.usedUp = true
		return repository.save(item)
	}

	fun markAsUsedBy(item: PurchaseItem, user: User): PurchaseItem {
		item.addUser(user)
		return repository.save(item)
	}

	fun markAsUnusedBy(item: PurchaseItem, user: User): PurchaseItem {
		item.removeUser(user)
		return repository.save(item)
	}

	fun userHasAccessTo(purchaseItem: PurchaseItem, user: User): Boolean {
		return purchaseItem.purchase.group.containsUser(user)
	}

	fun removeItem(itemId: UUID) {
		val item = find(itemId)
		repository.delete(item)
	}
}
