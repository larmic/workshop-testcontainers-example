package de.larmic.freitagsfruehstrueck.testcontainers.billing.client

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
internal class CrmClientIT {

    @Container
    private val crmContainer = KGenericContainer(IMAGE_NAME)
            .withExposedPorts(CONTAINER_PORT)
            .waitingFor(Wait.forHttp("/").forStatusCode(200))

    @Test
    internal fun `read customer`() {
        val customer = CrmClient(crmContainer.mappedUrl).readCustomer("1")

        assertThat(customer.id).isEqualTo("1")
        assertThat(customer.name).isEqualTo("Christel Grimm")
        assertThat(customer.iban).isEqualTo("DE11434350924181929806")
    }

    companion object Constants {
        const val IMAGE_NAME = "larmic/freitagsfruehstueck-testcontainers-crm-mock:latest"
        const val CONTAINER_PORT = 8080
    }
}

class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(DockerImageName.parse(imageName))

private val KGenericContainer.mappedUrl: String
    get() = "http://localhost:${this.getMappedPort(CrmClientIT.CONTAINER_PORT)}"