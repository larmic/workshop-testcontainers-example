package de.larmic.freitagsfruehstrueck.testcontainers.billing

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean

@SpringBootApplication
open class SpringBootElasticsearchOverHttpApplication

fun main(args: Array<String>) {
    runApplication<SpringBootElasticsearchOverHttpApplication>(*args)
}
