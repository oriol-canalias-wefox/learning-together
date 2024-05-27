package com.learning.shortener.admin.controller

import com.learning.shortener.admin.domain.response.RegisterResponse
import com.learning.shortener.admin.service.RegisterService
import com.learning.shortener.redirector.domain.request.RegisterRequest
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/register")
class RegisterController(
    private val registerService: RegisterService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun register(@Valid @RequestBody request: RegisterRequest): RegisterResponse {
        logger.info("RegisterController#register, request=$request")
        return RegisterResponse(registerService.register(request))
    }
}