/*
 * Copyright (C) 2020 Vitalijus Dobrovolskis
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

package com.dobrovolskis.commexp.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.14
 */
@Configuration
class WebMvcConfiguration(@Autowired val environment: Environment) : WebMvcConfigurer {
	override fun addCorsMappings(registry: CorsRegistry) {
		val config = registry.addMapping("/**")
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
			.allowCredentials(true)
			.allowedHeaders(
				"CONTENT-TYPE",
				"Authorization",
				"Accept",
				"Origin",
				"X-REQUESTED-WITH",
				"X-XSRF-TOKEN"
			)
		if (PROFILE_DEVELOPMENT in environment.activeProfiles) {
			config.allowedOrigins("http://localhost:4200")
		}
	}
}

const val PROFILE_DEVELOPMENT = "development"
