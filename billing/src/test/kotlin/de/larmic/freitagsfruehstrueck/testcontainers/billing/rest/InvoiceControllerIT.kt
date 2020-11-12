package de.larmic.freitagsfruehstrueck.testcontainers.billing.rest

import de.larmic.freitagsfruehstrueck.testcontainers.billing.testing.ComposeContextInitializer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
@ContextConfiguration(initializers = [ComposeContextInitializer::class])
internal class InvoiceControllerIT {

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    private val expectedInvoice = """
            Hi Christel Grimm,
            your invoice for bank account DE11434350924181929806 is created!
            Greets!""".trimIndent()

    @Test
    internal fun `happy path system integration test`() {
        val invoice = testRestTemplate.createInvoice(customerId = "1")

        assertThat(invoice).isEqualTo(expectedInvoice)
    }

    private fun TestRestTemplate.createInvoice(customerId: String): String {
        val response = this.exchange("/$customerId", HttpMethod.PUT, null, String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isNotBlank
        return response.body!!
    }
}