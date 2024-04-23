package com.learning.shortener.redirector.controller

import com.learning.shortener.redirector.domain.response.ErrorResponse
import com.learning.shortener.redirector.exceptions.RedirectorException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class HandlerController {
    private val logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(RedirectorException::class)
    fun redirectorExceptionHandler(redirectorException: RedirectorException): ResponseEntity<ErrorResponse> =
        ResponseEntity.status(redirectorException.status).body(ErrorResponse(redirectorException.message))

    @ExceptionHandler(Exception::class)
    fun exceptionHandler(ex: Exception): ResponseEntity<ErrorResponse> =
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse("unexpected error: ${ex.message}"))
            .also {
                logger.error("Unexpected error: ${ex.message}", ex)
            }
}