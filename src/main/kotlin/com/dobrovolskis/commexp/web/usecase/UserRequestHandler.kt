package com.dobrovolskis.commexp.web.usecase

import com.dobrovolskis.commexp.model.User

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.09
 */
interface UserRequestHandler<Result> {
	operator fun invoke(currentUser: User): Result
}
