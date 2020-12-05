package com.dobrovolskis.commexp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CommExpApplication

fun main(args: Array<String>) {
	runApplication<CommExpApplication>(*args)
}
