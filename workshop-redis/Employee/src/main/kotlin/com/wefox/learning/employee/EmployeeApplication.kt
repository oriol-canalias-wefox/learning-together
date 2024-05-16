package com.wefox.learning.employee

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@EnableCaching
@SpringBootApplication
class EmployeeApplication

fun main(args: Array<String>) {
	runApplication<EmployeeApplication>(*args)
}
