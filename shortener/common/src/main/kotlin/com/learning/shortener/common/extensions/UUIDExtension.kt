package com.learning.shortener.common.extensions

import java.util.UUID

fun UUID.toHash(): String = this.toString().takeLast(10)