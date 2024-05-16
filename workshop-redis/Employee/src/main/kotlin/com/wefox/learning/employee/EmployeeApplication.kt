package com.wefox.learning.employee

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories

@EnableCaching
@EnableRedisRepositories
@SpringBootApplication
class EmployeeApplication

fun main(args: Array<String>) {
	runApplication<EmployeeApplication>(*args)
}
