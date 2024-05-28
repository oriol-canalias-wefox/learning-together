package com.learning.shortener.admin.integration

import com.learning.shortener.admin.configuration.FakerConfiguration
import com.learning.shortener.admin.domain.response.RegisterResponse
import com.learning.shortener.common.constants.RabbitConstant
import com.learning.shortener.common.domain.message.RegisterUrl
import com.learning.shortener.common.extensions.buildWithPath
import io.restassured.RestAssured
import org.awaitility.Awaitility
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import java.net.URL
import java.util.concurrent.TimeUnit

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegisterIntegrationTest(
    connectionFactory: ConnectionFactory,
    private val rabbitTemplate: RabbitTemplate
) {
    private val rabbitAdmin = RabbitAdmin(connectionFactory)

    @LocalServerPort
    private var port: Int = 0

    @BeforeEach
    fun setup() {
        RestAssured.port = port
    }

    @Test
    fun `register new url without TTL, but set default one`() {
        val url = FakerConfiguration.FAKER.internet().url()

        val response = RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("""{ "url": "$url" }""")
            .`when`()
            .post("/register")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .body()
            .`as`(RegisterResponse::class.java)

        Awaitility
            .await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted {
                Assertions.assertEquals(
                    1,
                    rabbitAdmin.getQueueInfo(RabbitConstant.REGISTER_URL_QUEUE_NAME).messageCount
                )
                val message = rabbitTemplate.receiveAndConvert(RabbitConstant.REGISTER_URL_QUEUE_NAME)
                Assertions.assertNotNull(message)
                Assertions.assertInstanceOf(RegisterUrl::class.java, message)
                val registerUrl = message as RegisterUrl
                Assertions.assertEquals(24 * 60 * 60, registerUrl.ttlInSeconds)
                Assertions.assertEquals(response.hash, registerUrl.hash)
                Assertions.assertEquals(URL(url).buildWithPath(), registerUrl.url)
            }
    }

    @Test
    fun `register new url with TTL null`() {
        val url = FakerConfiguration.FAKER.internet().url()

        val response = RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("""{ "url": "$url", "ttlInSeconds": null }""")
            .`when`()
            .post("/register")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .body()
            .`as`(RegisterResponse::class.java)

        Awaitility
            .await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted {
                Assertions.assertEquals(
                    1,
                    rabbitAdmin.getQueueInfo(RabbitConstant.REGISTER_URL_QUEUE_NAME).messageCount
                )
                val message = rabbitTemplate.receiveAndConvert(RabbitConstant.REGISTER_URL_QUEUE_NAME)
                Assertions.assertNotNull(message)
                Assertions.assertInstanceOf(RegisterUrl::class.java, message)
                val registerUrl = message as RegisterUrl
                Assertions.assertNull(registerUrl.ttlInSeconds)
                Assertions.assertEquals(response.hash, registerUrl.hash)
                Assertions.assertEquals(URL(url).buildWithPath(), registerUrl.url)
            }
    }

    @Test
    fun `register withou url, throws bad request`() {
        RestAssured.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("""{ "url": null }""")
            .`when`()
            .post("/register")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())

        Assertions.assertEquals(
            0,
            rabbitAdmin.getQueueInfo(RabbitConstant.REGISTER_URL_QUEUE_NAME).messageCount
        )
    }
}