package com.dobrovolskis.commexp.web.usecase.purchase

import com.dobrovolskis.commexp.model.Purchase
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.service.PurchaseService
import com.dobrovolskis.commexp.service.UserGroupService
import com.dobrovolskis.commexp.web.request.PurchaseListRequest
import com.dobrovolskis.commexp.web.usecase.BaseRequestHandler
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.09
 */
@Service
@Transactional(readOnly = true)
class GetPurchasesInGroup(
	private val groupService: UserGroupService,
	private val purchaseService: PurchaseService
) : BaseRequestHandler<PurchaseListRequest, List<Purchase>> {
	override operator fun invoke(currentUser: User, request: PurchaseListRequest): List<Purchase> {
		val groupId = request.groupId
		val group = groupService.find(groupId)
		require(currentUser.isInGroup(group)) {
			"User not in group"
		}
		return purchaseService.getAllForGroup(group)
	}
}
