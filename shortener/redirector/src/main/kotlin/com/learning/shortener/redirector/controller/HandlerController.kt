package com.learning.shortener.redirector.controller

import com.learning.shortener.common.controller.BaseHandlerController
import com.learning.shortener.common.domain.response.ErrorResponse
import com.learning.shortener.redirector.exceptions.RedirectorException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class HandlerController: BaseHandlerController() {
    @ExceptionHandler(RedirectorException::class)
    fun redirectorExceptionHandler(redirectorException: RedirectorException): ResponseEntity<ErrorResponse> =
        ResponseEntity.status(redirectorException.status).body(ErrorResponse(redirectorException.message))
}