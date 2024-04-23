package com.learning.shortener.redirector.extensions

import java.util.UUID

fun UUID.toHash(): String = this.toString().takeLast(10)