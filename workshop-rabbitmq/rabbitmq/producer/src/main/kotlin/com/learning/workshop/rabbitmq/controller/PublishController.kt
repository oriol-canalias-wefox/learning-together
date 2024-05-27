package com.learning.workshop.rabbitmq.controller

import com.learning.workshop.rabbitmq.domain.Person
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/publish")
class PublishController(
    private val rabbitTemplate: RabbitTemplate
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping("string/{exchange}/{routingKey}")
    fun simplePublish(@PathVariable exchange: String,
                      @PathVariable routingKey: String,
                      @RequestBody request: String) {
        logger.info("simplePublish, exchange: $exchange, routingKey: $routingKey, request: $request")
        Thread.sleep(10_000)
        rabbitTemplate.convertAndSend(exchange, routingKey, request)
    }

    @PostMapping("object/{exchange}/{routingKey}")
    fun objectPublish(@PathVariable exchange: String,
                      @PathVariable routingKey: String,
                      @RequestBody request: Person) {
        logger.info("objectPublish, exchange: $exchange, routingKey: $routingKey, request: $request")
        rabbitTemplate.convertAndSend(exchange, routingKey, request)
    }
}