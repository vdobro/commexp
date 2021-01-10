package com.dobrovolskis.commexp.web.controller

import com.dobrovolskis.commexp.config.PATH_SHOPS
import com.dobrovolskis.commexp.model.Shop
import com.dobrovolskis.commexp.web.ControllerUtils
import com.dobrovolskis.commexp.web.dto.ShopDto
import com.dobrovolskis.commexp.web.request.ShopCreationRequest
import com.dobrovolskis.commexp.web.request.ShopListRequest
import com.dobrovolskis.commexp.web.usecase.purchase.GetShopsInGroup
import com.dobrovolskis.commexp.web.usecase.purchase.RegisterShop
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RestController

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
	fun getInMyGroup(@RequestBody request: ShopListRequest): List<ShopDto> =
		getShopsInGroup(getUser(), request).map(this::mapToDto)

	@RequestMapping(method = [POST])
	fun create(@RequestBody request: ShopCreationRequest): ShopDto =
		mapToDto(registerShop(getUser(), request))

	fun mapToDto(shop: Shop): ShopDto {
		return ShopDto(id = shop.id()!!, name = shop.name)
	}

	fun getUser() = controllerUtils.getCurrentUser()
}
