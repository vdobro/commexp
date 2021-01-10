package com.dobrovolskis.commexp

import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

/**
 * @author Vitalijus Dobrovolskis
 * @since 2021.01.09
 */
class ServletInitializer : SpringBootServletInitializer() {
	override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder {
		return application.sources(CommExpApplication::class.java)
	}
}
