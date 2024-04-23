package com.wefox.learning.employee.extensions

import com.wefox.learning.employee.domain.entity.Employee
import com.wefox.learning.employee.domain.request.EmployeeRequest
import com.wefox.learning.employee.domain.response.EmployeeResponse

fun EmployeeRequest.toEmployee() =
    Employee(
        name = this.name,
        age = this.age,
        job = this.job,
        height = this.height,
        weight = this.weight,
        description = this.description
    )

fun Employee.toResponse() =
    EmployeeResponse(
        id = this.id,
        name = this.name,
        age = this.age,
        job = this.job,
        height = this.height,
        weight = this.weight,
        description = this.description
    )

