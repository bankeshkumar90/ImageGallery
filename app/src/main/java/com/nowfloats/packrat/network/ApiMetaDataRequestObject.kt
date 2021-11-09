package com.nowfloats.packrat.network

data class ApiMetaDataRequestObject(
    val CollectionId: String,
    val CreatedBy: String,
    val CreatedByName: String,
    val products: List<products>
)