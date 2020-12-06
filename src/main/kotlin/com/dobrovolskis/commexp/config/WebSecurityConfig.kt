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

import com.dobrovolskis.commexp.repository.UserRepository
import com.dobrovolskis.commexp.service.ApplicationUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


/**
 * @author Vitalijus Dobrovolskis
 * @since 2020.12.06
 */
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

	@Bean
	fun userDetailsService(repository: UserRepository): UserDetailsService {
		return ApplicationUserDetailsService(repository)
	}

	@Bean
	fun passwordEncoder(): BCryptPasswordEncoder {
		return BCryptPasswordEncoder()
	}

	override fun configure(auth: AuthenticationManagerBuilder) {
		auth.userDetailsService(userDetailsService())
			.passwordEncoder(passwordEncoder())
	}

	override fun configure(http: HttpSecurity) {
		http
			.authorizeRequests()
				.antMatchers("/users").permitAll()
				.anyRequest().authenticated()
			.and()
				.httpBasic()
			.and()
				.formLogin().disable()
				.csrf().disable()
				.logout().disable()
	}
}