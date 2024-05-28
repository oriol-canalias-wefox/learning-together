package com.learning.shortener.redirector.service

import com.learning.shortener.common.domain.message.RegisterUrl
import com.learning.shortener.redirector.exceptions.ShorterNotFoundException
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RegisterService(private val redisTemplate: StringRedisTemplate) {

    fun register(registerUrl: RegisterUrl) {
        if (registerUrl.ttlInSeconds == null) {
            redisTemplate.boundValueOps(registerUrl.hash).set(registerUrl.url)
        } else {
            redisTemplate.boundValueOps(registerUrl.hash).set(
                registerUrl.url,
                Duration.ofSeconds(registerUrl.ttlInSeconds!!)
            )
        }
    }

    fun retrieve(hash: String): String {
        return redisTemplate.boundValueOps(hash).get() ?: throw ShorterNotFoundException(hash)
    }
}