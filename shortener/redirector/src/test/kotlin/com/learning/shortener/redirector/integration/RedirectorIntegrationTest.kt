package com.learning.shortener.redirector.integration

import com.learning.shortener.common.extensions.buildWithPath
import com.learning.shortener.common.extensions.toHash
import com.learning.shortener.redirector.configuration.FakerConfiguration
import io.restassured.RestAssured
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.HttpStatus
import java.net.URL
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RedirectorIntegrationTest(
    private val redisTemplate: StringRedisTemplate
) {

    @LocalServerPort
    private var port: Int = 0

    @BeforeEach
    fun setup() {
        RestAssured.port = port
    }

    @Test
    fun `redirection successfully`() {
        val url = URL(FakerConfiguration.FAKER.internet().url()).buildWithPath()
        val hash = UUID.fromString(FakerConfiguration.FAKER.internet().uuid()).toHash()
        redisTemplate.boundValueOps(hash).set(url)

        val response = RestAssured.given()
            .`when`()
            .redirects()
            .follow(false)
            .get(hash)
            .then()
            .statusCode(HttpStatus.MOVED_PERMANENTLY.value())
            .extract()
            .header("Location")

        assertEquals(url, response)
    }

    @Test
    fun `redirection with params`() {
        val url = URL(FakerConfiguration.FAKER.internet().url()).buildWithPath()
        val hash = UUID.fromString(FakerConfiguration.FAKER.internet().uuid()).toHash()
        redisTemplate.boundValueOps(hash).set(url)

        val response = RestAssured.given()
            .param("myparam", "myvalue")
            .`when`()
            .redirects()
            .follow(false)
            .get(hash)
            .then()
            .statusCode(HttpStatus.MOVED_PERMANENTLY.value())
            .extract()
            .header("Location")

        assertNotEquals(url, response)
        val parsedResponse = URL(response)
        assertEquals(url, parsedResponse.buildWithPath())
        assertEquals("myparam=myvalue", parsedResponse.query)
    }

    @Test
    fun `redirection non existing key`() {
        val hash = UUID.fromString(FakerConfiguration.FAKER.internet().uuid()).toHash()

        RestAssured.given()
            .`when`()
            .redirects()
            .follow(false)
            .get(hash)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
    }

}