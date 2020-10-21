package de.larmic.freitagsfruehstrueck.testcontainers.billing.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import khttp.get
import khttp.responses.Response
import org.json.JSONObject

@Service
class CrmClient(@Value("\${crm.rest.url}") private var crmUrl: String) {

    fun readCustomer(customerId: String) = get("$crmUrl/api/customer/$customerId").toCustomer()

    private fun Response.toCustomer() = this.jsonObject.toCustomer()
    private fun JSONObject.toCustomer() = Customer(id = this["id"] as String, name = this["name"] as String, iban = this["iban"] as String)
}

class Customer(val id: String,
               val name: String,
               val iban: String)