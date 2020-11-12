package de.larmic.freitagsfruehstrueck.testcontainers.billing.database

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import de.larmic.freitagsfruehstrueck.testcontainers.billing.database.InvoiceDocument.Companion.documentIndex
import org.elasticsearch.action.get.GetRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.common.xcontent.XContentType
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class InvoiceRepository(private val restHighLevelClient: RestHighLevelClient) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val mapper = ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(KotlinModule())

    fun store(invoice: String): String {
        logger.info("Store invoice '$invoice' to database")
        val request = InvoiceDocument(text = invoice).mapToRequest()
        return restHighLevelClient.index(request, RequestOptions.DEFAULT).id
    }

    fun findBy(id: String): InvoiceDocument? {
        logger.info("Load invoice '$id' from database")
        val getResponse = restHighLevelClient.getById(id)
        return when (getResponse.isExists) {
            true -> mapper.readValue(getResponse.sourceAsString, InvoiceDocument::class.java)
            false -> null
        }
    }

    private fun RestHighLevelClient.getById(id: String) = this.get(GetRequest(documentIndex, id), RequestOptions.DEFAULT)

    private fun InvoiceDocument.mapToRequest() : IndexRequest {
        val writeValueAsString = mapper.writeValueAsString(this)
        val request = IndexRequest(documentIndex)
        request.source(writeValueAsString, XContentType.JSON)
        return request
    }
}