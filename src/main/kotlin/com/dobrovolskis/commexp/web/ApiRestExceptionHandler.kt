/*
 * Copyright (C) 2021 Vitalijus Dobrovolskis
 *
 * This file is part of commexp.
 *
 * commexp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, version 3 of the License.
 *
 * commexp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with commexp; see the file LICENSE. If not,
 * see <https://www.gnu.org/licenses/>.
 *
 * SPDX-License-Identifier: AGPL-3.0-only
 */

package com.dobrovolskis.commexp.web

import com.dobrovolskis.commexp.exception.ResourceAccessError
import com.dobrovolskis.commexp.exception.ResourceNotFoundError
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.09.10
 */
@ControllerAdvice
class ApiRestExceptionHandler : ResponseEntityExceptionHandler() {

	@ExceptionHandler(IllegalArgumentException::class)
	protected fun handleException(e: IllegalArgumentException, request: WebRequest): ResponseEntity<Any> =
		handle(e, BAD_REQUEST, request)

	@ExceptionHandler(ResourceNotFoundError::class)
	protected fun handleException(e: ResourceNotFoundError, request: WebRequest): ResponseEntity<Any> =
		handle(e, NOT_FOUND, request)

	@ExceptionHandler(ResourceAccessError::class)
	protected fun handleException(e: ResourceAccessError, request: WebRequest): ResponseEntity<Any> =
		handle(e, FORBIDDEN, request)

	@ExceptionHandler(IllegalStateException::class)
	protected fun handleException(e: IllegalStateException, request: WebRequest): ResponseEntity<Any> =
		handle(e, UNAUTHORIZED, request)

	private fun handle(
		cause: Throwable,
		status: HttpStatus,
		request: WebRequest
	) = handleExceptionInternal(
		cause as Exception,
		ValidationResponse(cause.message ?: FALLBACK_ERROR_MESSAGE),
		headers(),
		status, request
	)
}

private fun headers(): HttpHeaders {
	val headers = HttpHeaders()
	headers.contentType = MediaType.APPLICATION_JSON
	return headers
}

data class ValidationResponse(
	val error: String,
)

private const val FALLBACK_ERROR_MESSAGE = "Unknown error"
