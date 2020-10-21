package de.larmic.freitagsfruehstrueck.testcontainers.billing.rest

import de.larmic.freitagsfruehstrueck.testcontainers.billing.testing.ComposeContextInitializer
import de.larmic.freitagsfruehstrueck.testcontainers.billing.testing.createIndex
import de.larmic.freitagsfruehstrueck.testcontainers.billing.testing.deleteIndexIfExists
import org.assertj.core.api.Assertions.assertThat
import org.elasticsearch.client.RestHighLevelClient
import org.junit.jupiter.api.BeforeEach
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

    @Autowired
    private lateinit var restHighLevelClient: RestHighLevelClient

    @BeforeEach
    fun setUp() {
        restHighLevelClient.deleteIndexIfExists().createIndex()
    }

    @Test
    internal fun `happy path system integration test`() {
        val invoiceId = testRestTemplate.createInvoice(customerId = "1")
        val invoice = testRestTemplate.getInvoice(invoiceId = invoiceId)

        assertThat(invoice).isEqualTo(expectedInvoice)
    }

    private val expectedInvoice = """
            Hi Christel Grimm,
            your invoice for bank account DE11434350924181929806 is created!
            Greets!""".trimIndent()

    private fun TestRestTemplate.createInvoice(customerId: String) : String {
        val response = this.exchange("/$customerId", HttpMethod.PUT, null, String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isNotBlank
        return response.body!!
    }

    private fun TestRestTemplate.getInvoice(invoiceId: String): String {
        val response = this.exchange("/$invoiceId", HttpMethod.GET, null, String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isNotBlank
        return response.body!!
    }
}