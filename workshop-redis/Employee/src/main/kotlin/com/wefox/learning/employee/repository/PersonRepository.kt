package com.wefox.learning.employee.repository

import com.wefox.learning.employee.domain.entity.Person
import org.springframework.data.repository.CrudRepository

interface PersonRepository: CrudRepository<Person, String>