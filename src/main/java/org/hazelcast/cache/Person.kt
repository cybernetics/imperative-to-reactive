package org.hazelcast.cache

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Sort
import org.springframework.data.repository.reactive.ReactiveSortingRepository
import org.springframework.web.reactive.function.server.*
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono
import java.time.LocalDate


@Configuration
class PersonRoutes {

    @Bean
    fun router(repository: PersonRepository) = router {
        val handler = PersonHandler(repository)
        GET("/person", handler::getAll)
        GET("/person/{id}", handler::getOne)
    }
}

class PersonHandler(private val repository: PersonRepository) {

    fun getAll(req: ServerRequest): Mono<ServerResponse> {
        val flux = repository.findAll(Sort.by("lastName", "firstName"))
        return ok().body<Person>(flux)
    }

    fun getOne(req: ServerRequest): Mono<ServerResponse> {
        val mono = repository.findById(req.pathVariable("id").toLong())
        return ok().body<Person>(mono)
    }
}

interface PersonRepository : ReactiveSortingRepository<Person, Long>

class Person (
    @Id
    val id: Long,
    var firstName: String,
    var lastName: String,
    var birthdate: LocalDate? = null
)