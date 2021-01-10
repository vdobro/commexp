package com.dobrovolskis.commexp.web.usecase.purchase

import com.dobrovolskis.commexp.model.Shop
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.service.ShopService
import com.dobrovolskis.commexp.service.UserGroupService
import com.dobrovolskis.commexp.web.request.ShopListRequest
import com.dobrovolskis.commexp.web.usecase.BaseRequestHandler
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.09
 */
@Service
@Transactional(readOnly = true)
class GetShopsInGroup(
	private val groupService: UserGroupService,
	private val shopService: ShopService
) : BaseRequestHandler<ShopListRequest, List<Shop>> {
	override operator fun invoke(currentUser: User, request: ShopListRequest): List<Shop> {
		val groupId = request.groupId
		val group = groupService.find(groupId)
		require(currentUser.isInGroup(group)) {
			"User not in group"
		}
		return shopService.getAllForGroup(group)
	}
}
