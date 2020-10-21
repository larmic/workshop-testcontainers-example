package de.larmic.freitagsfruehstrueck.testcontainers.billing.database

class InvoiceDocument(val text: String) {
    companion object {
        const val documentIndex = "billing"
    }
}