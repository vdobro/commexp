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

import com.dobrovolskis.commexp.model.BatchImport
import com.dobrovolskis.commexp.model.ImportedEntityType
import com.dobrovolskis.commexp.web.dto.BatchImportResultDto
import com.opencsv.bean.AbstractBeanField
import com.opencsv.bean.AbstractCsvConverter
import com.opencsv.bean.CsvBindAndJoinByName
import com.opencsv.bean.CsvBindByName
import com.opencsv.bean.CsvCustomBindByName
import com.opencsv.bean.CsvDate
import com.opencsv.bean.CsvToBeanBuilder
import org.apache.commons.collections4.MultiValuedMap
import org.springframework.stereotype.Service
import java.io.InputStream
import java.io.InputStreamReader
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.03.16
 */
@Service
class CsvParserUtils {

	fun parsePurchases(inputStream: InputStream): List<PurchaseEntry> =
		prepareParser(inputStream)

	fun parseItems(inputStream: InputStream): List<PurchaseItemEntry> =
		prepareParser(inputStream)

	fun mapImportToResultDto(
		entities: List<UUID>,
		importSession: BatchImport
	) = BatchImportResultDto(
		sessionId = importSession.id()!!,
		created = importSession.created,
		author = importSession.importedBy.id()!!,
		itemType = ImportedEntityType.PURCHASE,
		itemIds = entities,
	)

	private inline fun <reified T> prepareParser(inputStream: InputStream): List<T> {
		val reader = InputStreamReader(inputStream)
		val csvToBean = CsvToBeanBuilder<T>(reader)
			.withType(T::class.java)
			.withSeparator(';')
			.withIgnoreLeadingWhiteSpace(true)
			.build()
		return csvToBean.parse()
	}
}


data class PurchaseEntry(
	@CsvBindByName(column = "Einkauf")
	var id: String? = null,

	@CsvBindByName(column = "Wer")
	var buyer: String? = null,

	@CsvBindByName(column = "Datum")
	@CsvDate("yyyy-MM-dd")
	var date: LocalDate? = null,

	@CsvBindByName(column = "Wo")
	var shop: String? = null,

	@CsvCustomBindByName(
		column = "Summe",
		converter = PriceAmountConverter::class
	)
	var sum: BigDecimal? = null,

	@CsvBindAndJoinByName(
		column = ".*",
		elementType = String::class
	)
	var rest: MultiValuedMap<String, String>? = null,
)

data class PurchaseItemEntry(
	@CsvBindByName(column = "Nr")
	var purchaseId: String? = null,

	@CsvBindByName(column = "Name")
	var name: String? = null,

	@CsvCustomBindByName(
		column = "Preis",
		converter = PriceAmountConverter::class
	)
	var price: BigDecimal? = null,

	@CsvBindAndJoinByName(
		column = "benutzer-.*",
		elementType = Boolean::class,
		converter = UsageConverter::class
	)
	var users: MultiValuedMap<String, Boolean>? = null,

	@CsvBindAndJoinByName(
		column = ".*",
		elementType = String::class
	)
	var rest: MultiValuedMap<String, String>? = null,
)

class UsageConverter : AbstractCsvConverter() {
	override fun convertToRead(p0: String): Boolean {
		return p0 == "x"
	}
}

class PriceAmountConverter : AbstractBeanField<String, BigDecimal>() {
	override fun convert(p0: String): BigDecimal {
		return p0
			.trimEnd('€')
			.trim()
			.replace(',', '.')
			.toBigDecimal()
	}
}
