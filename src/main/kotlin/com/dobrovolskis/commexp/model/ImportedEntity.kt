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

import com.dobrovolskis.commexp.config.Constraints.Strings.LENGTH_SHORT
import com.dobrovolskis.commexp.config.Table.IMPORTED_ENTITIES
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.02.16
 */
@Entity
@Table(name = IMPORTED_ENTITIES)
class ImportedEntity(

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "import_id",
		nullable = false,
		updatable = false
	)
	var import: BatchImport,

	@Enumerated(EnumType.STRING)
	var type: ImportedEntityType,

	@NotNull
	@Column(
		name = "entity_id",
		updatable = false,
		nullable = false
	)
	var entityId: UUID,

	@NotNull
	@Column(
		name = "original_id",
		updatable = false,
		nullable = false
	)
	@Size(max = LENGTH_SHORT)
	var originalId: String,

	) : IdEntity() {
}

enum class ImportedEntityType {
	PURCHASE,
	ITEM
}
