package com.learning.shortener.admin.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.learning.shortener.common.constants.RabbitConstant.EXCHANGE_NAME
import com.learning.shortener.common.constants.RabbitConstant.REGISTER_URL_DLQ_NAME
import com.learning.shortener.common.constants.RabbitConstant.REGISTER_URL_DLQ_ROUTING_KEY
import com.learning.shortener.common.constants.RabbitConstant.REGISTER_URL_QUEUE_NAME
import com.learning.shortener.common.constants.RabbitConstant.REGISTER_URL_QUEUE_ROUTING_KEY
import jakarta.annotation.PostConstruct
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Exchange
import org.springframework.amqp.core.ExchangeBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfiguration(
    private val connectionFactory: ConnectionFactory
) {
    @Bean
    fun messageConverter(objectMapper: ObjectMapper): MessageConverter =
        Jackson2JsonMessageConverter(objectMapper)

    @PostConstruct
    fun createQueues() {
        val rabbitAdmin = RabbitAdmin(connectionFactory)

        val directExchange = createDirectExchange(EXCHANGE_NAME)
        rabbitAdmin.declareExchange(directExchange)

        val registerUrlDlq = createQueue( REGISTER_URL_DLQ_NAME)
        val registerUrlQueue = createQueue( REGISTER_URL_QUEUE_NAME, EXCHANGE_NAME, REGISTER_URL_DLQ_ROUTING_KEY)
        rabbitAdmin.declareQueue(registerUrlDlq)
        rabbitAdmin.declareQueue(registerUrlQueue)

        val bindingRegisterQueue = createBinding(directExchange, registerUrlQueue,  REGISTER_URL_QUEUE_ROUTING_KEY)
        val bindingRegisterDlq = createBinding(directExchange, registerUrlDlq, REGISTER_URL_DLQ_ROUTING_KEY)
        rabbitAdmin.declareBinding(bindingRegisterQueue)
        rabbitAdmin.declareBinding(bindingRegisterDlq)
    }

    private fun createQueue(name: String, dlqExchange: String? = null, dlqRoutingKey: String? = null): Queue {
        val builder = QueueBuilder.durable(name)
        dlqExchange?.let { builder.deadLetterExchange(it) }
        dlqRoutingKey?.let { builder.deadLetterRoutingKey(it) }
        return builder.build()
    }

    private fun createDirectExchange(name: String): Exchange =
        ExchangeBuilder
            .directExchange(name)
            .build()

    private fun createBinding(exchange: Exchange, queue: Queue, bindingKey: String): Binding =
        BindingBuilder.bind(queue).to(exchange).with(bindingKey).noargs()
}