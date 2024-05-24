package com.learning.workshop.rabbitmq.subscriber

import com.learning.workshop.rabbitmq.domain.Person
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service
import java.nio.charset.Charset

@Service
class ConsumerService {
    private val logger = LoggerFactory.getLogger(javaClass)

    @RabbitListener(queues = ["queue1"])
    fun consume(message: Person) {
        logger.info("consumer message $message")
//        val body = message.body.toString(Charset.defaultCharset())
//        logger.info("consumer message body: $body")
    }
}