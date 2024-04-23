package com.learning.shortener.redirector.extensions

import java.net.URL

fun URL.buildWithPath(): String = "${this.protocol}://${this.host}${this.path?:""}"