package de.larmic.freitagsfruehstrueck.testcontainers.billing.testing

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName

class CrmMockContextInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(context: ConfigurableApplicationContext) {
        crmMockContainer.start()

        val mappedPort = crmMockContainer.getMappedPort(8080)

        TestPropertyValues.of(
                "crm.rest.url=http://localhost:$mappedPort"
        ).applyTo(context.environment)
    }

    companion object {
        private val crmMockContainer = KGenericContainer("larmic/freitagsfruehstueck-testcontainers-crm-mock:latest")
                .withExposedPorts(8080)
                .waitingFor(Wait.forHttp("/").forStatusCode(200))
    }
}

class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(DockerImageName.parse(imageName))