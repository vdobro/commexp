package com.dobrovolskis.commexp.service

import com.dobrovolskis.commexp.model.Shop
import com.dobrovolskis.commexp.model.UserGroup
import com.dobrovolskis.commexp.repository.ShopRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.09
 */
@Service
@Transactional
class ShopService(private val repository: ShopRepository) {

	fun createNew(
		group: UserGroup,
		name: String
	): Shop = repository.save(
		Shop(
			name = name,
			group = group
		)
	)

	fun getAllForGroup(group: UserGroup): List<Shop> {
		return repository.getAllByGroup(group)
	}
}
