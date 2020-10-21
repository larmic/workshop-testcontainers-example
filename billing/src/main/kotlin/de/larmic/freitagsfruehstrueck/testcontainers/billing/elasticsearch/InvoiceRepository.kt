package de.larmic.freitagsfruehstrueck.testcontainers.billing.elasticsearch

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import de.larmic.freitagsfruehstrueck.testcontainers.billing.elasticsearch.InvoiceDocument.Companion.documentIndex
import org.elasticsearch.action.get.GetRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.common.xcontent.XContentType
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Service
class InvoiceRepository(private val restHighLevelClient: RestHighLevelClient) {

    private val mapper = ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .registerModule(KotlinModule())

    fun store(invoice: String): String {
        val document = InvoiceDocument(text = invoice)
        val writeValueAsString = mapper.writeValueAsString(document)
        val request = IndexRequest(documentIndex)
        request.source(writeValueAsString, XContentType.JSON)
        return restHighLevelClient.index(request, RequestOptions.DEFAULT).id
    }

    fun findBy(id: String): InvoiceDocument? {
        val getRequest = GetRequest(documentIndex, id)
        val getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT)
        return when (getResponse.isExists) {
            true -> {
                val sourceAsString = getResponse.sourceAsString
                return mapper.readValue(sourceAsString, InvoiceDocument::class.java)
            }
            false -> null
        }
    }
}