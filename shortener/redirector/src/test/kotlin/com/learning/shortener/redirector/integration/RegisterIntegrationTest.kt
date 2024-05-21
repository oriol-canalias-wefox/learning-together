package com.learning.shortener.redirector.integration

import com.learning.shortener.redirector.configuration.FakerConfiguration
import com.learning.shortener.redirector.domain.response.RegisterResponse
import com.learning.shortener.redirector.extensions.buildWithPath
import io.restassured.RestAssured
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import java.net.URL

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegisterIntegrationTest(
    private val redisTemplate: StringRedisTemplate
) {

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

        val redisEntity = redisTemplate.boundValueOps(response.hash)
        assertNotNull(redisEntity.get())
        assertEquals(URL(url).buildWithPath(), redisEntity.get())
        assertNotNull(redisEntity.expire)
        assertTrue(24 * 60 * 60 >= redisEntity.expire!!)
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

        val redisEntity = redisTemplate.boundValueOps(response.hash)
        assertNotNull(redisEntity.get())
        assertEquals(URL(url).buildWithPath(), redisEntity.get())
        assertNotNull(redisEntity.expire)
        assertEquals(-1, redisEntity.expire!!)
    }
}