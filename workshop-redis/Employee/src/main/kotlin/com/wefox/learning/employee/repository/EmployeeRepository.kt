package com.wefox.learning.employee.repository

import com.wefox.learning.employee.domain.entity.Employee
import org.springframework.data.jpa.repository.JpaRepository

interface EmployeeRepository: JpaRepository<Employee, Long> {
    fun existsByName(name: String): Boolean
}