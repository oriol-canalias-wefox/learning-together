package com.wefox.learning.employee.controller

import com.wefox.learning.employee.domain.entity.Person
import com.wefox.learning.employee.repository.PersonRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/persons")
class PersonController(
    private val personRepository: PersonRepository
) {
    @GetMapping("/{id}")
    fun getPerson(@PathVariable id: String): Person =
        personRepository.findById(id).orElseThrow()

    @PostMapping
    fun getPerson(@RequestBody person: Person): Person =
        personRepository.save(person)
}