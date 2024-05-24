package com.learning.workshop.rabbitmq.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Exchange
import org.springframework.amqp.core.ExchangeBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QueueConfiguration {
    @Bean
    fun directExchange(): Exchange =
        ExchangeBuilder
            .directExchange("my-exchange")
            .build()

    @Bean
    fun queue(): Queue =
        QueueBuilder
            .durable("queue1")
            .lazy()
            .build()

    @Bean
    fun binding(): Binding =
        BindingBuilder
            .bind(queue())
            .to(directExchange())
            .with("to-1")
            .noargs()

    @Bean
    fun messageConverter(objectMapper: ObjectMapper): MessageConverter =
        Jackson2JsonMessageConverter(objectMapper)
}