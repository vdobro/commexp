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

import com.dobrovolskis.commexp.config.ID_COLUMN_NAME
import org.springframework.data.util.ProxyUtils
import java.util.UUID
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.persistence.Transient

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.06
 */
@MappedSuperclass
abstract class IdEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = ID_COLUMN_NAME, updatable = false)
	private var _id: UUID? = null

	@Transient
	fun id() = _id

	override fun equals(other: Any?): Boolean {
		other ?: return false
		if (this === other) return true
		if (javaClass != ProxyUtils.getUserClass(other)) return false

		other as IdEntity
		val id = this.id()
		id ?: return false
		return id == other.id()
	}

	override fun hashCode(): Int {
		return 29
	}

	override fun toString() =
		"Entity of type ${this.javaClass.name} with id: $_id"
}