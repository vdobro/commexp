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

import com.dobrovolskis.commexp.config.PATH_SHOPS
import com.dobrovolskis.commexp.model.Shop
import com.dobrovolskis.commexp.web.ControllerUtils
import com.dobrovolskis.commexp.web.dto.ShopDto
import com.dobrovolskis.commexp.web.request.ShopCreationRequest
import com.dobrovolskis.commexp.web.usecase.purchase.GetShopsInGroup
import com.dobrovolskis.commexp.web.usecase.purchase.RegisterShop
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.validation.Valid

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.09
 */
@RestController
@RequestMapping(value = [PATH_SHOPS])
class ShopController(
	private val getShopsInGroup: GetShopsInGroup,
	private val registerShop: RegisterShop,
	private val controllerUtils: ControllerUtils
) {
	@RequestMapping(method = [GET])
	fun getInMyGroup(@RequestParam(required = true) groupId: UUID): List<ShopDto> =
		getShopsInGroup(getUser(), groupId).map(this::mapToDto)

	@RequestMapping(method = [POST])
	fun create(@RequestBody @Valid request: ShopCreationRequest): ShopDto =
		mapToDto(registerShop(getUser(), request))

	private fun mapToDto(shop: Shop) = ShopDto(id = shop.id()!!, name = shop.name)

	private fun getUser() = controllerUtils.getCurrentUser()
}
