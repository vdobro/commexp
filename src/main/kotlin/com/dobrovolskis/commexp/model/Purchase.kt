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

package com.dobrovolskis.commexp.model

import com.dobrovolskis.commexp.config.TABLE_PURCHASES
import org.hibernate.annotations.CreationTimestamp
import java.time.ZonedDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType.LAZY
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.persistence.Transient
import javax.validation.constraints.NotNull

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.05
 */
@Entity
@Table(name = TABLE_PURCHASES)
class Purchase(
	@NotNull
	@ManyToOne(fetch = LAZY)
	@JoinColumn(name = "shop_id")
	var shop: Shop,

	@NotNull
	@ManyToOne(fetch = LAZY)
	@JoinColumn(
		name = "user_id",
		nullable = false
	)
	var doneBy: User,

	@NotNull
	@ManyToOne(fetch = LAZY)
	@JoinColumn(
		name = "creator_id",
		nullable = false
	)
	var createdBy: User,

	@NotNull
	@ManyToOne(fetch = LAZY)
	@JoinColumn(
		name = "group_id",
		nullable = false,
		updatable = false,
	)
	var group: UserGroup,

	@NotNull
	@Column(
		name = "shopping_timestamp",
		nullable = false
	)
	val shoppingTime: ZonedDateTime,

	) : IdEntity() {

	@NotNull
	@Column(
		name = "created",
		updatable = false,
		nullable = false
	)
	@CreationTimestamp
	var created: ZonedDateTime? = null

	@OneToMany(
		targetEntity = PurchaseItem::class,
		fetch = LAZY,
		mappedBy = "purchase",
		orphanRemoval = true,
		cascade = [CascadeType.ALL]
	)
	private val _items: MutableList<PurchaseItem> = mutableListOf()

	@Transient
	fun items() = _items.toList()

	fun addItem(item: PurchaseItem) {
		_items.add(item)
	}
}
