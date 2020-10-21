package de.larmic.freitagsfruehstrueck.testcontainers.billing.elasticsearch

import de.larmic.freitagsfruehstrueck.testcontainers.billing.testing.ElasticsearchContextInitializer
import de.larmic.freitagsfruehstrueck.testcontainers.billing.testing.createIndex
import de.larmic.freitagsfruehstrueck.testcontainers.billing.testing.deleteIndexIfExists
import de.larmic.freitagsfruehstrueck.testcontainers.billing.testing.refreshIndex
import org.assertj.core.api.Assertions.assertThat
import org.elasticsearch.client.RestHighLevelClient
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@ActiveProfiles("it")
@ContextConfiguration(initializers = [ElasticsearchContextInitializer::class])
internal class InvoiceRepositoryIT {

    @Autowired
    private lateinit var restHighLevelClient: RestHighLevelClient

    @Autowired
    private lateinit var invoiceRepository: InvoiceRepository

    @BeforeEach
    fun setUp() {
        restHighLevelClient.deleteIndexIfExists().createIndex()
    }

    @Test
    fun `store and load an invoice document`() {
        restHighLevelClient.refreshIndex()

        val id = invoiceRepository.store("Invoice-test")
        assertThat(id).isNotBlank

        val invoice = invoiceRepository.findBy(id)!!
        assertThat(invoice.text).isEqualTo("Invoice-test")
    }
}