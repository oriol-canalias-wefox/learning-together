package com.wefox.learning.employee.service

import com.wefox.learning.employee.domain.request.EmployeeRequest
import com.wefox.learning.employee.domain.response.EmployeeResponse
import com.wefox.learning.employee.extensions.toEmployee
import com.wefox.learning.employee.extensions.toResponse
import com.wefox.learning.employee.repository.EmployeeRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class EmployeeService(
    private val employeeRepository: EmployeeRepository
) {

    fun save(employeeRequest: EmployeeRequest) {
        if (employeeRepository.existsByName(employeeRequest.name)) {
            throw RuntimeException("Employee already exists")
        }
        employeeRepository.save(employeeRequest.toEmployee())
    }

    @Cacheable("employees")
    fun findById(id: Long): EmployeeResponse =
        employeeRepository.findById(id).orElseThrow().toResponse()
}