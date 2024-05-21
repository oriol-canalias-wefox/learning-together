package com.learning.shortener.redirector.service

import com.learning.shortener.redirector.domain.request.RegisterRequest
import com.learning.shortener.redirector.exceptions.ShorterNotFoundException
import com.learning.shortener.redirector.extensions.buildWithPath
import com.learning.shortener.redirector.extensions.toHash
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.net.URL
import java.time.Duration
import java.util.UUID

@Service
class RegisterService(private val redisTemplate: StringRedisTemplate) {

    fun register(request: RegisterRequest): String {
        val parsedURL = URL(request.url).buildWithPath()
        val key = UUID.nameUUIDFromBytes(parsedURL.toByteArray()).toHash()

        if (request.ttlInSeconds == null) {
            redisTemplate.boundValueOps(key).set(parsedURL)
        } else {
            redisTemplate.boundValueOps(key).set(parsedURL, Duration.ofSeconds(request.ttlInSeconds))
        }
        return key
    }

    fun retrieve(hash: String): String {
        return redisTemplate.boundValueOps(hash).get() ?: throw ShorterNotFoundException(hash)
    }
}