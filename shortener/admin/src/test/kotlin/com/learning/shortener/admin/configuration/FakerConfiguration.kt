package com.learning.shortener.admin.configuration

import net.datafaker.Faker
import java.util.Random

object FakerConfiguration {
    val FAKER = Faker(Random(System.currentTimeMillis()))
}