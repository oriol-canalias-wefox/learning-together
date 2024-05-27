package com.learning.shortener.common.domain.message

data class RegisterUrl(
    val hash: String,
    val url: String,
    val ttlInSeconds: Long?
)
