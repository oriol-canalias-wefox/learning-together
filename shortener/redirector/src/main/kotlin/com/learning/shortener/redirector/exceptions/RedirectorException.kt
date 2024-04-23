package com.learning.shortener.redirector.exceptions

import org.springframework.http.HttpStatus

abstract class RedirectorException(message: String, val status: HttpStatus) : RuntimeException(message)