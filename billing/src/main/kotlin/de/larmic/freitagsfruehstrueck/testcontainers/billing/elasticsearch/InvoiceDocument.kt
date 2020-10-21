package de.larmic.freitagsfruehstrueck.testcontainers.billing.elasticsearch

class InvoiceDocument(val text: String) {
    companion object {
        const val documentIndex = "billing"
    }
}