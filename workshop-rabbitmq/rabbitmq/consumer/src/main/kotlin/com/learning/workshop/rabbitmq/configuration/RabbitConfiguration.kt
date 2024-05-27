package com.learning.workshop.rabbitmq.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.learning.workshop.rabbitmq.subscriber.MyMessageListener
import org.aopalliance.aop.Advice
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.MessageListenerContainer
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfiguration {
    @Bean
    fun messageConverter(objectMapper: ObjectMapper): MessageConverter =
        Jackson2JsonMessageConverter(objectMapper)

    @Bean
    fun listener(connectionFactory: ConnectionFactory,
                 myMessageListener: MyMessageListener): MessageListenerContainer {
        val listener = SimpleMessageListenerContainer()
        listener.connectionFactory = connectionFactory
        listener.setQueueNames("queue2")
        listener.setMessageListener(myMessageListener)
        listener.setAdviceChain(retryPolicy())
        listener.setConcurrency("10")
        listener.setPrefetchCount(10)
        return listener
    }

    private fun retryPolicy() : Advice {
        return RetryInterceptorBuilder
            .stateless()
            .maxAttempts(5)
            .backOffOptions(
                1_000,
                2.0,
                6_000
            )
            .recoverer(RejectAndDontRequeueRecoverer())
            .build()
    }
}