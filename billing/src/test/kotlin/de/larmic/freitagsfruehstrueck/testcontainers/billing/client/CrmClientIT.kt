package de.larmic.freitagsfruehstrueck.testcontainers.billing.client

import de.larmic.freitagsfruehstrueck.testcontainers.billing.testing.ComposeContextInitializer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [CrmClient::class])
@ActiveProfiles("it")
@ContextConfiguration(initializers = [ComposeContextInitializer::class])
internal class CrmClientIT {

    @Autowired
    private lateinit var crmClient: CrmClient

    @Test
    internal fun `read customer`() {
        val customer = crmClient.readCustomer("1")

        assertThat(customer.id).isEqualTo("1")
        assertThat(customer.name).isEqualTo("Christel Grimm")
        assertThat(customer.iban).isEqualTo("DE11434350924181929806")
    }
}