package com.dobrovolskis.commexp.web.usecase.item

import com.dobrovolskis.commexp.model.PurchaseItem
import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.service.PurchaseService
import com.dobrovolskis.commexp.web.request.ItemListRequest
import com.dobrovolskis.commexp.web.usecase.BaseRequestHandler
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.09
 */
@Service
@Transactional(readOnly = true)
class GetItemsInPurchase(
	private val purchaseService: PurchaseService,
) : BaseRequestHandler<ItemListRequest, List<PurchaseItem>> {
	override operator fun invoke(
		currentUser: User,
		request: ItemListRequest
	): List<PurchaseItem> {
		val purchase = purchaseService.find(request.purchaseId)
		require(currentUser.isInGroup(purchase.group)) {
			"User has no access to the purchase"
		}
		return purchase.items()
	}
}
