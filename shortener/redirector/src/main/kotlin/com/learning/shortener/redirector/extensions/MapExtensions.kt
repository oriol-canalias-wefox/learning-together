package com.learning.shortener.redirector.extensions

fun Map<String, String>?.toQueryParams(): String {
    if (this.isNullOrEmpty()) {
        return ""
    }
    return "?" + this.entries.joinToString("&") { "${it.key}=${it.value}" }
}