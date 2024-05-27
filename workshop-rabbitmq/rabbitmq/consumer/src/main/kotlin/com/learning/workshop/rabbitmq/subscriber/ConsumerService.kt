package com.learning.workshop.rabbitmq.subscriber

import com.learning.workshop.rabbitmq.domain.Person
import org.slf4j.LoggerFactory
import org.springframework.amqp.AmqpRejectAndDontRequeueException
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service
import java.nio.charset.Charset

@Service
class ConsumerService {
    private val logger = LoggerFactory.getLogger(javaClass)

    @RabbitListener(queues = ["queue1"], concurrency = "10-100")
    fun consume(message: Person) {
        logger.info("consumer message $message")
//        val body = message.body.toString(Charset.defaultCharset())
//        logger.info("consumer message body: $body")
    }

//    @RabbitListener(queues = ["queue2"])
    fun consumeQueue2(message: Person) {
        logger.info("consumer2 message $message")
        if (message.age == null) {
            logger.warn("Age is not informed")
            throw AmqpRejectAndDontRequeueException("Age is not informed")
        }
        logger.info("consumer finalize successfully")
    }
}