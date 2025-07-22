package com.devtaco.fashion

import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FashionApplication

private val log = KotlinLogging.logger {}


fun main(args: Array<String>) {
	runApplication<FashionApplication>(*args)
}
