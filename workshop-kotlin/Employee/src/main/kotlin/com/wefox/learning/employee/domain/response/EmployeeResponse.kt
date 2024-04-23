package com.wefox.learning.employee.domain.response

data class EmployeeResponse(
    val id: Int,
    val name: String,
    val age: Int,
    val job: String,
    val height: Int,
    val weight: Int,
    val description: String? = null
)
