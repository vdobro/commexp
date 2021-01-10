package com.dobrovolskis.commexp.web.usecase.user

import com.dobrovolskis.commexp.model.User
import com.dobrovolskis.commexp.model.UserGroup
import com.dobrovolskis.commexp.web.usecase.UserRequestHandler
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.09
 */
@Service
@Transactional(readOnly = true)
class GetUserGroupList : UserRequestHandler<List<UserGroup>> {
	override operator fun invoke(currentUser: User): List<UserGroup> {
		return currentUser.userGroups
	}
}
