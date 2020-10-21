package de.larmic.freitagsfruehstrueck.testcontainers.billing.testing

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.elasticsearch.ElasticsearchContainer
import java.io.File

class ComposeContextInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(context: ConfigurableApplicationContext) {
        composeContainer.start()

        val elasticsearch = composeContainer.getContainerByServiceName("elasticsearch_1").get()
        val crm = composeContainer.getContainerByServiceName("crm_1").get()

        TestPropertyValues.of(
                "spring.elasticsearch.rest.uris=${elasticsearch.containerIpAddress}:${elasticsearch.getMappedPort(9200)}",
                "crm.rest.url=http://localhost:${crm.getMappedPort(8080)}"
        ).applyTo(context.environment)
    }

    companion object {
        private val composeContainer = KDockerComposeContainer(File("src/test/resources/docker-compose-test.yml"))
                .withExposedService("elasticsearch_1", 9200, Wait.forHttp("/").forStatusCode(200))
                .withExposedService("crm_1", 8080, Wait.forHttp("/").forStatusCode(200))
    }
}

class KDockerComposeContainer(file: File) : DockerComposeContainer<KDockerComposeContainer>(file)