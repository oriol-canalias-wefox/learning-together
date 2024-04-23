package com.learning.shortener.redirector.exceptions

import org.springframework.http.HttpStatus

class ShorterNotFoundException(shorter: String) :
    RedirectorException("The shorter $shorter is not found or is expired", HttpStatus.NOT_FOUND)