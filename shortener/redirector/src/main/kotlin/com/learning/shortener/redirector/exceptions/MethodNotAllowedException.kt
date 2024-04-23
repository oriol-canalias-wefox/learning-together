package com.learning.shortener.redirector.exceptions

import org.springframework.http.HttpStatus

class MethodNotAllowedException :
    RedirectorException("The http method is not supported for redirect", HttpStatus.METHOD_NOT_ALLOWED)