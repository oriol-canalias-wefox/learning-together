package com.learning.shortener.redirector.extensions

import java.net.URL

fun URL.buildWithPath(): String =
    if (this.port > 0) {
        "${this.protocol}://${this.host}:${this.port}${this.path ?: ""}"
    } else {
        "${this.protocol}://${this.host}${this.path ?: ""}"
    }