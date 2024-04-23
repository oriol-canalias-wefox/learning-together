package com.learning.shortener.redirector.service

import com.learning.shortener.redirector.domain.request.RegisterRequest
import com.learning.shortener.redirector.exceptions.ShorterNotFoundException
import com.learning.shortener.redirector.extensions.buildWithPath
import com.learning.shortener.redirector.extensions.toHash
import org.springframework.stereotype.Service
import java.net.URL
import java.util.UUID

@Service
class RegisterService {
    companion object {
        private val registers = mutableMapOf<String, String>()
    }

    fun register(request: RegisterRequest): String {
        val parsedURL = URL(request.url).buildWithPath()
        return UUID.nameUUIDFromBytes(parsedURL.toByteArray()).toHash().also {
            registers[it] = parsedURL
        }
    }

    fun retrieve(hash: String): String {
        return registers[hash] ?: throw ShorterNotFoundException(hash)
    }
}