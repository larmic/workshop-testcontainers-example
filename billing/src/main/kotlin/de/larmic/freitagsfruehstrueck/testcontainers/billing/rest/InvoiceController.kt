package de.larmic.freitagsfruehstrueck.testcontainers.billing.rest

import de.larmic.freitagsfruehstrueck.testcontainers.billing.client.CrmClient
import de.larmic.freitagsfruehstrueck.testcontainers.billing.client.Customer
import de.larmic.freitagsfruehstrueck.testcontainers.billing.database.InvoiceRepository
import org.springframework.web.bind.annotation.*

@RestController
class InvoiceController(private val crmClient: CrmClient,
                        private val invoiceRepository: InvoiceRepository) {

    @PutMapping("/{customerId}")
    fun createInvoiceForCustomer(@PathVariable customerId: String): String {
        val customer = crmClient.readCustomer(customerId)
        val invoice = createInvoice(customer)
        return invoiceRepository.store(invoice)
    }

    @GetMapping("/{invoiceId}")
    fun getInvoice(@PathVariable invoiceId: String): String {
        return invoiceRepository.findBy(invoiceId)?.text.orEmpty()
    }

    private fun createInvoice(customer: Customer) = """
            Hi ${customer.name},
            your invoice for bank account ${customer.iban} is created!
            Greets!""".trimIndent()

}