package com.learning.shortener.common.constants

object RabbitConstant {
    const val REGISTER_URL_QUEUE_NAME = "register-url"
    const val REGISTER_URL_DLQ_NAME = "register-url-dlq"
    const val EXCHANGE_NAME = "direct-exchange"
    const val REGISTER_URL_QUEUE_ROUTING_KEY = "to-register-url"
    const val REGISTER_URL_DLQ_ROUTING_KEY = "to-register-url-dlq"
}