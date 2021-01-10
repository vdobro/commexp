package com.dobrovolskis.commexp.web.request

import java.util.UUID

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.09
 */
data class ShopCreationRequest(
	val groupId: UUID,
	val name: String
)
