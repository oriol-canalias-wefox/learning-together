package com.wefox.learning.employee.domain.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

data class EmployeeRequest(
    @field:NotBlank
    val name: String,
    @field:Min(16)
    val age: Int,
    @field:NotBlank
    val job: String,
    @field:Positive
    val height: Int,
    @field:Positive
    val weight: Int,
    val description: String? = null
)
