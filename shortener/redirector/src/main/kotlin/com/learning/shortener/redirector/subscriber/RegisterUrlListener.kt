package com.learning.shortener.redirector.subscriber

import com.learning.shortener.common.constants.RabbitConstant
import com.learning.shortener.common.domain.message.RegisterUrl
import com.learning.shortener.redirector.service.RegisterService
import org.slf4j.LoggerFactory
import org.springframework.amqp.AmqpRejectAndDontRequeueException
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class RegisterUrlListener(
    private val registerService: RegisterService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @RabbitListener(queues = [RabbitConstant.REGISTER_URL_QUEUE_NAME])
    fun consume(message: RegisterUrl) {
        logger.info("RegisterUrlListener#consume, message=$message")
        runCatching {
            registerService.register(message)
        }.onFailure {
            logger.warn("RegisterUrlListener#consume, error processing message", it)
            throw AmqpRejectAndDontRequeueException(it)
        }
    }
}