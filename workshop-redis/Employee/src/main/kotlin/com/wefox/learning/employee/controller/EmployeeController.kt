package com.wefox.learning.employee.controller

import com.wefox.learning.employee.domain.request.EmployeeRequest
import com.wefox.learning.employee.domain.response.EmployeeResponse
import com.wefox.learning.employee.service.EmployeeService
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.time.Duration

@RestController
@RequestMapping("employees")
class EmployeeController(
    private val employeeService: EmployeeService,
    private val redisTemplate: RedisTemplate<String, Boolean>
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("test")
    fun test(): String = "hello world"

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@Valid @RequestBody employeeRequest: EmployeeRequest,
             request: HttpServletRequest) {
        logger.info("save $employeeRequest")
        val operationAllowed = redisTemplate.boundValueOps(request.remoteAddr)
            .setIfAbsent(true, Duration.ofSeconds(2)) ?: true
        if (operationAllowed.not()) {
            throw ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests")
        }
        employeeService.save(employeeRequest)
    }

    @GetMapping("{id}")
    fun find(@PathVariable id: Long): EmployeeResponse =
        employeeService.findById(id)
}