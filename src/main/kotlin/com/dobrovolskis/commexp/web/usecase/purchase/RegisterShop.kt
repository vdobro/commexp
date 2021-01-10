package com.dobrovolskis.commexp.web.usecase.purchase

import com.dobrovolskis.commexp.model.Shop
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.service.ShopService
import com.dobrovolskis.commexp.service.UserGroupService
import com.dobrovolskis.commexp.web.request.ShopCreationRequest
import com.dobrovolskis.commexp.web.usecase.BaseRequestHandler
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.09
 */
@Service
@Transactional
class RegisterShop(
	private val shopService: ShopService,
	private val groupService: UserGroupService,
) : BaseRequestHandler<ShopCreationRequest, Shop> {
	override fun invoke(currentUser: User, request: ShopCreationRequest): Shop {
		val group = groupService.find(request.groupId)
		require(currentUser.isInGroup(group)) {
			"User not in group"
		}
		return shopService.createNew(group = group, name = request.name)
	}
}
