package com.wefox.learning.employee.service

import com.wefox.learning.employee.domain.entity.Employee
import com.wefox.learning.employee.domain.request.EmployeeRequest
import com.wefox.learning.employee.domain.response.EmployeeResponse
import com.wefox.learning.employee.extensions.toEmployee
import com.wefox.learning.employee.extensions.toResponse
import com.wefox.learning.employee.repository.EmployeeRepository
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

    fun findById(id: Long): EmployeeResponse =
        employeeRepository.findByIdNullable(id)?.let{
            it.toResponse()
        }?: throw RuntimeException()
}