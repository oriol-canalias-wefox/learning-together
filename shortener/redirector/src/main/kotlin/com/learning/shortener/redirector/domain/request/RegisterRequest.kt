package com.learning.shortener.redirector.domain.request

import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.URL

data class RegisterRequest(
    @field:NotBlank
    @field:URL
    val url: String,
    val ttlInSeconds: Long? = 24 * 60 * 60
)
