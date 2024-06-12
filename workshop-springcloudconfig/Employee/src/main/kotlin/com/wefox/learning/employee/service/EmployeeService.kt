package com.wefox.learning.employee.service

import com.wefox.learning.employee.domain.request.EmployeeRequest
import com.wefox.learning.employee.domain.response.EmployeeResponse
import com.wefox.learning.employee.extensions.toEmployee
import com.wefox.learning.employee.extensions.toResponse
import com.wefox.learning.employee.repository.EmployeeRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class EmployeeService(
    private val employeeRepository: EmployeeRepository,
    @Value("\${employee.description.mandatory:false}")
    private val descriptionMandatory: Boolean
) {

    fun save(employeeRequest: EmployeeRequest) {
        if (descriptionMandatory && employeeRequest.description == null) {
            throw RuntimeException("Description is mandatory")
        }
        if (employeeRepository.existsByName(employeeRequest.name)) {
            throw RuntimeException("Employee already exists")
        }
        employeeRepository.save(employeeRequest.toEmployee())
    }

    fun findById(id: Long): EmployeeResponse =
        employeeRepository.findByIdOrNull(id)?.toResponse() ?: throw RuntimeException()
}