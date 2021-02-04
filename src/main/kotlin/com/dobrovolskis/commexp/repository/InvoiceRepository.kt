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

package com.dobrovolskis.commexp.repository

import com.dobrovolskis.commexp.model.Invoice
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.model.UserGroup
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime
import java.util.UUID

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.06
 */
@Repository
interface InvoiceRepository : CrudRepository<Invoice, UUID> {

	fun findAllByFromIsGreaterThanEqualAndToLessThanEqualAndGroupAndPayer(
		from: ZonedDateTime, to: ZonedDateTime, group: UserGroup, payer: User
	): Iterable<Invoice>

	fun findByFromAndToAndPayerAndReceiverAndGroup(
		from: ZonedDateTime, to: ZonedDateTime, payer: User, receiver: User, group: UserGroup
	): Invoice?

	@Query("from Invoice i where i.group = :group and i.from <= :date and i.to >= :date")
	fun findAllWithDateIn(
		@Param("date") date: ZonedDateTime,
		@Param("group") group: UserGroup
	): List<Invoice>

	@Query(
		"from Invoice i where i.group = :group and i.payer = :payer and i.receiver = :receiver and i.from <= :date and :date <= i.to"
	)
	fun existsAnyForUserPairWithDateIn(
		@Param("date") date: ZonedDateTime,
		@Param("group") group: UserGroup,
		@Param("payer") payer: User,
		@Param("receiver") receiver: User,
	): List<Invoice>
}
