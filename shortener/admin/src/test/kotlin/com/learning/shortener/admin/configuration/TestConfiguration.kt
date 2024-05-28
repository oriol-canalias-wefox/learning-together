package com.learning.shortener.admin.configuration

import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.utility.DockerImageName

@Configuration
class TestConfiguration {
    @Bean
    @ServiceConnection
    fun rabbitContainer(): RabbitMQContainer =
        RabbitMQContainer(DockerImageName.parse("rabbitmq:3"))
            .withAdminPassword("testpass")
}