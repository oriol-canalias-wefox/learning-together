package com.learning.shortener.redirector.integration

import com.learning.shortener.common.constants.RabbitConstant
import com.learning.shortener.common.domain.message.RegisterUrl
import com.learning.shortener.common.extensions.toHash
import com.learning.shortener.redirector.configuration.FakerConfiguration
import io.restassured.RestAssured
import org.awaitility.Awaitility
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.data.redis.core.StringRedisTemplate
import java.util.*
import java.util.concurrent.TimeUnit

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegisterIntegrationTest(
    private val rabbitTemplate: RabbitTemplate,
    private val redisTemplate: StringRedisTemplate
) {
    @LocalServerPort
    private var port: Int = 0

    @BeforeEach
    fun setup() {
        RestAssured.port = port
    }

    @Test
    fun `register new url with TTL`() {
        val registerUrl = RegisterUrl(
            hash = UUID.randomUUID().toHash(),
            url = FakerConfiguration.FAKER.internet().url(),
            ttlInSeconds = FakerConfiguration.FAKER.number().numberBetween(10L, 100L)
        )

        rabbitTemplate.convertAndSend(
            RabbitConstant.EXCHANGE_NAME,
            RabbitConstant.REGISTER_URL_QUEUE_ROUTING_KEY,
            registerUrl
        )

        Awaitility
            .await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted {
                val redisEntity = redisTemplate.boundValueOps(registerUrl.hash)
                assertNotNull(redisEntity.get())
                assertEquals(registerUrl.url, redisEntity.get())
                assertNotNull(redisEntity.expire)
                assertTrue(registerUrl.ttlInSeconds!! >= redisEntity.expire!!)
            }
    }

    @Test
    fun `register new url with TTL null`() {
        val registerUrl = RegisterUrl(
            hash = UUID.randomUUID().toHash(),
            url = FakerConfiguration.FAKER.internet().url(),
            ttlInSeconds = null
        )

        rabbitTemplate.convertAndSend(
            RabbitConstant.EXCHANGE_NAME,
            RabbitConstant.REGISTER_URL_QUEUE_ROUTING_KEY,
            registerUrl
        )

        Awaitility
            .await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted {
                val redisEntity = redisTemplate.boundValueOps(registerUrl.hash)
                assertNotNull(redisEntity.get())
                assertEquals(registerUrl.url, redisEntity.get())
                assertNotNull(redisEntity.expire)
                assertEquals(-1, redisEntity.expire!!)
            }
    }
}