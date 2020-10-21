package de.larmic.freitagsfruehstrueck.testcontainers.billing.testing

import de.larmic.freitagsfruehstrueck.testcontainers.billing.database.InvoiceDocument
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.indices.CreateIndexRequest
import org.elasticsearch.client.indices.GetIndexRequest

fun RestHighLevelClient.refreshIndex(): RestHighLevelClient {
    val request = RefreshRequest(InvoiceDocument.documentIndex)
    this.indices().refresh(request, RequestOptions.DEFAULT)
    return this
}

fun RestHighLevelClient.deleteIndexIfExists(): RestHighLevelClient {
    if (this.indexExists(InvoiceDocument.documentIndex)) {
        this.indices().delete(DeleteIndexRequest(InvoiceDocument.documentIndex), RequestOptions.DEFAULT)
    }
    return this
}

fun RestHighLevelClient.createIndex(): RestHighLevelClient {
    this.indices().create(CreateIndexRequest(InvoiceDocument.documentIndex), RequestOptions.DEFAULT)
    return this
}

private fun RestHighLevelClient.indexExists(index: String) = this.indices().exists(GetIndexRequest(index), RequestOptions.DEFAULT)
