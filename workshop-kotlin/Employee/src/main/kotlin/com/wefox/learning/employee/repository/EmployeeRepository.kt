package com.wefox.learning.employee.repository

import com.wefox.learning.employee.domain.entity.Employee
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface EmployeeRepository: JpaRepository<Employee, Long> {
    fun existsByName(name: String): Boolean

    @Query("from Employee where id=:id")
    fun findByIdNullable(id: Long): Employee?
}