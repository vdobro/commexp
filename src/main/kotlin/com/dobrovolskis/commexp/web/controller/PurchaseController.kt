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

package com.dobrovolskis.commexp.web.controller

import com.dobrovolskis.commexp.config.PATH_PURCHASES
import com.dobrovolskis.commexp.model.Purchase
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.web.ControllerUtils
import com.dobrovolskis.commexp.web.dto.BatchImportResultDto
import com.dobrovolskis.commexp.web.dto.PurchaseDto
import com.dobrovolskis.commexp.web.request.ImportRequest
import com.dobrovolskis.commexp.web.request.PurchaseCreationRequest
import com.dobrovolskis.commexp.web.usecase.BatchImporter
import com.dobrovolskis.commexp.web.usecase.purchase.CreatePurchase
import com.dobrovolskis.commexp.web.usecase.purchase.GetPurchasesInGroup
import com.dobrovolskis.commexp.web.usecase.purchase.RemovePurchase
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.UUID
import javax.validation.Valid

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.06
 */
@RestController
@RequestMapping(value = [PATH_PURCHASES])
class PurchaseController(
	private val createPurchase: CreatePurchase,
	private val importer: BatchImporter,
	private val purchaseList: GetPurchasesInGroup,
	private val controllerUtils: ControllerUtils,
	private val removePurchase: RemovePurchase,
) {
	@PostMapping
	fun createNew(@RequestBody @Valid creationRequest: PurchaseCreationRequest): PurchaseDto {
		val result = createPurchase(getUser(), creationRequest)
		return mapToDto(result)
	}

	@PostMapping(path = ["/import"])
	fun import(
		@RequestParam("group") groupId: UUID,
		@RequestParam("file") file: MultipartFile) : BatchImportResultDto {
		val request = ImportRequest(groupId = groupId, file = file)
		return importer.importPurchases(user = getUser(), request = request)
	}

	@GetMapping
	fun getAll(@RequestParam(required = true) groupId: UUID): List<PurchaseDto> {
		val result = purchaseList(getUser(), groupId)
		return result.map(this::mapToDto)
	}

	@DeleteMapping(path = ["/{id}"])
	fun removePurchase(@PathVariable id: UUID) {
		removePurchase(getUser(), id)
	}

	private fun mapToDto(purchase: Purchase): PurchaseDto {
		return PurchaseDto(
			id = purchase.id()!!,
			shopId = purchase.shop.id()!!,
			time = purchase.shoppingTime,
			creation = purchase.created,
			doneBy = purchase.doneBy.id()!!
		)
	}

	private fun getUser(): User = controllerUtils.getCurrentUser()
}

