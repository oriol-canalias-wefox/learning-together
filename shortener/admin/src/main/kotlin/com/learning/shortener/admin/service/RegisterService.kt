package com.learning.shortener.admin.service

import com.learning.shortener.common.constants.RabbitConstant
import com.learning.shortener.common.domain.message.RegisterUrl
import com.learning.shortener.common.extensions.buildWithPath
import com.learning.shortener.common.extensions.toHash
import com.learning.shortener.redirector.domain.request.RegisterRequest
import org.springframework.amqp.core.Exchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import java.net.URL
import java.util.*

@Service
class RegisterService(
    private val rabbitTemplate: RabbitTemplate
) {

    fun register(request: RegisterRequest): String {
        val parsedURL = URL(request.url).buildWithPath()
        val key = UUID.nameUUIDFromBytes(parsedURL.toByteArray()).toHash()

        val message = RegisterUrl(hash = key, url = parsedURL, ttlInSeconds = request.ttlInSeconds)

        rabbitTemplate.convertAndSend(RabbitConstant.EXCHANGE_NAME, RabbitConstant.REGISTER_URL_QUEUE_ROUTING_KEY, message)
        return key
    }
}