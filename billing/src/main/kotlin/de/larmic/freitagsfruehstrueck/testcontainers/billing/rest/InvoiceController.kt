package de.larmic.freitagsfruehstrueck.testcontainers.billing.rest

import de.larmic.freitagsfruehstrueck.testcontainers.billing.client.CrmClient
import de.larmic.freitagsfruehstrueck.testcontainers.billing.client.Customer
import de.larmic.freitagsfruehstrueck.testcontainers.billing.database.InvoiceRepository
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
class InvoiceController(private val crmClient: CrmClient,
                        private val invoiceRepository: InvoiceRepository) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @PutMapping("/{customerId}")
    fun createInvoiceForCustomer(@PathVariable customerId: String): String {
        logger.info("Create invoice for customer '$customerId'")
        val customer = crmClient.readCustomer(customerId)
        val invoiceId = createInvoice(customer)
        return invoiceRepository.findInvoice(invoiceId)
    }

    private fun createInvoice(customer: Customer) = """
            Hi ${customer.name},
            your invoice for bank account ${customer.iban} is created!
            Greets!"""
            .trimIndent()
            .storeInDatabase()

    private fun String.storeInDatabase() = invoiceRepository.store(this)
    private fun InvoiceRepository.findInvoice(id: String) = this.findBy(id)?.text.orEmpty()

}