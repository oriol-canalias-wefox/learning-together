package com.learning.workshop.rabbitmq.configuration

import jakarta.annotation.PostConstruct
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Exchange
import org.springframework.amqp.core.ExchangeBuilder
import org.springframework.amqp.core.QueueBuilder
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfiguration(
    private val connectionFactory: ConnectionFactory
) {
    @PostConstruct
    fun createQueues() {
        val rabbitAdmin = RabbitAdmin(connectionFactory)

        val queue2 = QueueBuilder
            .durable("queue2")
            .ttl(100_000)
            .maxLength(10L)
            .deadLetterExchange("direct-exchange")
            .deadLetterRoutingKey("to-dlq")
            .build()

        val dlq = QueueBuilder
            .durable("dlq")
            .build()
        rabbitAdmin.declareQueue(queue2)
        rabbitAdmin.declareQueue(dlq)

        val exchange = ExchangeBuilder
            .directExchange("direct-exchange")
            .build<Exchange>()
        rabbitAdmin.declareExchange(exchange)

        rabbitAdmin.declareBinding(BindingBuilder
            .bind(dlq)
            .to(exchange)
            .with("to-dlq")
            .noargs())

        rabbitAdmin.declareBinding(BindingBuilder
            .bind(queue2)
            .to(exchange)
            .with("to-2")
            .noargs())
    }
}