package de.larmic.freitagsfruehstrueck.testcontainers.billing.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import khttp.get
import khttp.responses.Response
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

@Service
class CrmClient(@Value("\${crm.rest.url}") private var crmUrl: String) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun readCustomer(customerId: String) : Customer {
        logger.info("Load customer '$customerId' from crm")
        return get("$crmUrl/api/customer/$customerId").decodeToCustomer()
    }

}

private fun Response.decodeToCustomer() = Json.decodeFromString<Customer>(this.text)

@Serializable
class Customer(val id: String,
               val name: String,
               val iban: String)