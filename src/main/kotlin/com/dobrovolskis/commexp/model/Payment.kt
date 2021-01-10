package com.dobrovolskis.commexp.model

import com.dobrovolskis.commexp.config.TABLE_PAYMENTS
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.time.ZonedDateTime.now
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.Digits
import javax.validation.constraints.NotNull

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.10
 */
@Entity
@Table(name = TABLE_PAYMENTS)
class Payment(
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "payer_id",
		nullable = false,
		updatable = false,
	)
	var payer: User,

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name = "receiver_id",
		nullable = false,
		updatable = false,
	)
	var receiver: User,

	@NotNull
	@Column(
		name = "sum",
		nullable = false,
	)
	@Digits(integer = 10, fraction = 2)
	var sum: BigDecimal,

	) : IdEntity() {

	@NotNull
	@Column(
		name = "created",
		updatable = false,
		nullable = false
	)
	var created: ZonedDateTime = now()
}
