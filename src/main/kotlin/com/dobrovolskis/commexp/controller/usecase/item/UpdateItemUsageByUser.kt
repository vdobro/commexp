package com.dobrovolskis.commexp.controller.usecase.item

import com.dobrovolskis.commexp.controller.request.ItemUsageChangeRequest
import com.dobrovolskis.commexp.controller.usecase.BaseRequestHandler
import com.dobrovolskis.commexp.model.PurchaseItem
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.service.PurchaseItemService
import com.dobrovolskis.commexp.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.06
 */
@Service
@Transactional
class UpdateItemUsageByUser(
	private val itemService: PurchaseItemService,
	private val userService: UserService,
) : BaseRequestHandler<ItemUsageChangeRequest, PurchaseItem> {

	override fun invoke(currentUser: User,
	                    request: ItemUsageChangeRequest): PurchaseItem {
		validateRequest(currentUser, request)

		val user = userService.getById(request.user)
		val item = itemService.find(request.purchaseItem)

		return if (request.use) {
			itemService.markAsUsedBy(item, user)
		} else {
			itemService.markAsUnusedBy(item, user)
		}
	}

	private fun validateRequest(user: User, request: ItemUsageChangeRequest) {
		val item = itemService.find(request.purchaseItem)
		require(itemService.userHasAccessTo(item, user)) {
			"User ${user.username} access to purchase item ${request.purchaseItem} denied"
		}
	}
}