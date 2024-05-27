package com.learning.shortener.common.controller

import com.learning.shortener.common.domain.response.ErrorResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler

abstract class BaseHandlerController {
    private val logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(Exception::class)
    fun exceptionHandler(ex: Exception): ResponseEntity<ErrorResponse> =
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse("unexpected error: ${ex.message}"))
            .also {
                logger.error("Unexpected error: ${ex.message}", ex)
            }
}