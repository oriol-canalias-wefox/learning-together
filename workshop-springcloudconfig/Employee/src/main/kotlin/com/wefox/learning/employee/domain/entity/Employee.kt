package com.wefox.learning.employee.domain.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import org.apache.logging.log4j.util.StringMap

@Entity
data class Employee(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    val name: String,
    val age: Int,
    val job: String,
    val height: Int,
    val weight: Int,
    val description: String? = null
)
