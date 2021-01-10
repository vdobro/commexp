package com.dobrovolskis.commexp.web.dto

import java.util.UUID

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.10
 */
data class InvoiceItemDto(
	val itemId: UUID,
	val name: String,
	val purchaseId: UUID,
	val fullPrice: Int,
	val paidBy: UUID,

	val partToPay: Double,
	val partPrice: Int
)
