package com.learning.shortener.redirector.configuration

import com.learning.shortener.common.constants.RabbitConstant
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Exchange
import org.springframework.amqp.core.ExchangeBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.QueueBuilder
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.utility.DockerImageName

@Configuration
class TestConfiguration {

    @Bean
    @ServiceConnection(name = "redis")
    fun redisContainer(): GenericContainer<*> {
        return GenericContainer(DockerImageName.parse("redis:latest"))
            .withExposedPorts(6379)
    }

    @Bean
    @ServiceConnection
    fun rabbitContainer(): RabbitMQContainer =
        RabbitMQContainer(DockerImageName.parse("rabbitmq:3"))
            .withAdminPassword("testpass")

    @Bean
    fun directExchange(): Exchange =
        ExchangeBuilder
            .directExchange(RabbitConstant.EXCHANGE_NAME)
            .build()

    @Bean
    fun queue(): Queue =
        QueueBuilder
            .durable(RabbitConstant.REGISTER_URL_QUEUE_NAME)
            .deadLetterExchange(RabbitConstant.EXCHANGE_NAME)
            .deadLetterRoutingKey(RabbitConstant.REGISTER_URL_DLQ_ROUTING_KEY)
            .build()

    @Bean
    fun dlq(): Queue =
        QueueBuilder
            .durable(RabbitConstant.REGISTER_URL_DLQ_NAME)
            .build()

    @Bean
    fun bindingQueue(): Binding =
        BindingBuilder
            .bind(queue())
            .to(directExchange())
            .with(RabbitConstant.REGISTER_URL_QUEUE_ROUTING_KEY)
            .noargs()

    @Bean
    fun bindingDlq(): Binding =
        BindingBuilder
            .bind(dlq())
            .to(directExchange())
            .with(RabbitConstant.REGISTER_URL_DLQ_ROUTING_KEY)
            .noargs()

}