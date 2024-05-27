package com.learning.workshop.rabbitmq.subscriber

import com.learning.workshop.rabbitmq.domain.Person
import org.slf4j.LoggerFactory
import org.springframework.amqp.AmqpRejectAndDontRequeueException
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageListener
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.stereotype.Component

@Component
class MyMessageListener(
    private val messageConverter: MessageConverter
) : MessageListener {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun onMessage(message: Message) {
        logger.info("consumer using listener message $message")
        val person = messageConverter.fromMessage(message) as Person
        if (person.age == null) {
            logger.warn("Age is not informed")
            throw RuntimeException("Age is not informed")
        }
        logger.info("consumer finalize successfully")
    }
}