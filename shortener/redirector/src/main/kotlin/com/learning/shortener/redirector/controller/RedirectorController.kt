package com.learning.shortener.redirector.controller

import com.learning.shortener.redirector.exceptions.MethodNotAllowedException
import com.learning.shortener.redirector.extensions.toQueryParams
import com.learning.shortener.redirector.service.RegisterService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class RedirectorController(private val registerService: RegisterService) {

    @GetMapping(path = ["{shortPath}"])
    fun redirect(
        @PathVariable shortPath: String,
        @RequestParam requestParameters: Map<String, String>?
    ): ResponseEntity<Any> {
        val redirectUrl = registerService.retrieve(shortPath) + requestParameters.toQueryParams()
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, redirectUrl).build()
    }

    @RequestMapping(
        path = ["{anything}"],
        method = [RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH, RequestMethod.PATCH]
    )
    fun badRedirect(@PathVariable(required = false) anything: String?): ResponseEntity<Any> =
        throw MethodNotAllowedException()
}