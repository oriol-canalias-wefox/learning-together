package com.wefox.learning.springserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.config.server.EnableConfigServer

@EnableConfigServer
@SpringBootApplication
class SpringServerApplication

fun main(args: Array<String>) {
	runApplication<SpringServerApplication>(*args)
}
