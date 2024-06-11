package com.wefox.learning.employee.controller

import com.wefox.learning.employee.domain.request.EmployeeRequest
import com.wefox.learning.employee.domain.response.EmployeeResponse
import com.wefox.learning.employee.service.EmployeeService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("employees")
class EmployeeController(
    private val employeeService: EmployeeService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @GetMapping("test")
    fun test(): String = "hello world"

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@Valid @RequestBody employeeRequest: EmployeeRequest) {
        logger.info("save $employeeRequest")
        employeeService.save(employeeRequest)
    }

    @GetMapping("{id}")
    fun find(@PathVariable id: Long): EmployeeResponse =
        employeeService.findById(id)
}