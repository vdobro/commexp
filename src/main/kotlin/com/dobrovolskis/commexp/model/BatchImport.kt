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

import com.dobrovolskis.commexp.config.ColumnType
import com.dobrovolskis.commexp.config.Table.IMPORTS
import java.time.ZonedDateTime
import java.time.ZonedDateTime.now
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.persistence.Transient
import javax.validation.constraints.NotNull

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.02.16
 */
@Entity
@Table(name = IMPORTS)
class BatchImport(
	@NotNull
	@Column(
		name = "created",
		columnDefinition = ColumnType.MOMENT_WITH_TIMEZONE,
		nullable = false,
		updatable = false
	)
	var created: ZonedDateTime = now(),

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "imported_by",
		nullable = false,
		updatable = false,
	)
	var importedBy: User,

	) : IdEntity() {

	@OneToMany(
		targetEntity = ImportedEntity::class,
		fetch = FetchType.LAZY,
		mappedBy = "import",
		orphanRemoval = true,
		cascade = [CascadeType.ALL]
	)
	private val _items: MutableList<ImportedEntity> = mutableListOf()

	@Transient
	fun items() = _items.toList()

	fun addItem(item: ImportedEntity) {
		_items.add(item)
	}
}
