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

import com.dobrovolskis.commexp.model.PurchaseItem
import com.dobrovolskis.commexp.model.UserGroup
import org.hibernate.search.engine.search.query.SearchResult
import org.hibernate.search.mapper.orm.Search
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.03.23
 */
@Service
@Transactional
class IndexedItemSearchService(private val entityManager: EntityManager) {

	@Suppress("UNCHECKED_CAST")
	fun searchFirst(text: String, group: UserGroup): List<PurchaseItem> {
		val groupId = group.id()!!
		val searchSession = Search.session(entityManager)

		val result = searchSession.search(PurchaseItem::class.java)
			.where {
				it.bool()
					.filter(it.match().field("purchase.group._id").matching(groupId))
					.must(it.match().fields("name", "description", "purchase.shop.name").matching(text).fuzzy())
			}
			.sort { it
				.field("usedUp").asc().then()
				.field("purchase.shoppingTime").desc()
			}
			.fetch(20) as SearchResult<PurchaseItem>
		return result.hits().toList()
	}

	@EventListener(ApplicationReadyEvent::class)
	fun indexAll() {
		val session = Search.session(entityManager)
		session.massIndexer().startAndWait()
	}
}
