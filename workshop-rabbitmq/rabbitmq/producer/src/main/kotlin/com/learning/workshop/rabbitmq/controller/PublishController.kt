package com.learning.workshop.rabbitmq.controller

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/publish")
class PublishController {
    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping("{exchange}/{routingKey}")
    fun publish(@PathVariable exchange: String,
                @PathVariable routingKey: String,
                @RequestBody payload: String) {
        logger.info("publish, exchange: $exchange, routingKey: $routingKey, payload: $payload")
    }
}