package com.wefox.learning.employee.domain.entity

import jakarta.persistence.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("person")
data class Person(
    @Id
    var id: String,
    var name: String,
    var age: Int
)
