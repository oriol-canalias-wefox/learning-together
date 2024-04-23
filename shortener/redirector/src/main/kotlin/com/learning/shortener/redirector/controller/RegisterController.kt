package com.learning.shortener.redirector.controller

import com.learning.shortener.redirector.domain.request.RegisterRequest
import com.learning.shortener.redirector.domain.response.RegisterResponse
import com.learning.shortener.redirector.service.RegisterService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/register")
class RegisterController(
    private val registerService: RegisterService
) {

    @PostMapping
    fun register(@Valid @RequestBody registerRequest: RegisterRequest): RegisterResponse {
        val url = registerService.register(registerRequest)
        return RegisterResponse(url)
    }
}