package com.dobrovolskis.commexp.web.usecase.item

import com.dobrovolskis.commexp.model.PurchaseItem
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.service.InvoiceService
import com.dobrovolskis.commexp.service.PurchaseItemService
import com.dobrovolskis.commexp.service.UserService
import com.dobrovolskis.commexp.web.request.ItemUsageChangeRequest
import com.dobrovolskis.commexp.web.usecase.BaseRequestHandler
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
	private val invoiceService: InvoiceService,
) : BaseRequestHandler<ItemUsageChangeRequest, PurchaseItem> {

	override fun invoke(currentUser: User,
	                    request: ItemUsageChangeRequest): PurchaseItem {
		validateRequest(currentUser, request)

		val user = userService.getById(request.userId)
		val item = itemService.find(request.itemId)

		val result = if (request.use) {
			itemService.markAsUsedBy(item, user)
		} else {
			itemService.markAsUnusedBy(item, user)
		}
		invoiceService.reassembleIfAnyExistForChangedItem(item)
		return result
	}

	private fun validateRequest(user: User, request: ItemUsageChangeRequest) {
		val item = itemService.find(request.itemId)
		require(itemService.userHasAccessTo(item, user)) {
			"User ${user.username} access to purchase item ${request.itemId} denied"
		}
	}
}
