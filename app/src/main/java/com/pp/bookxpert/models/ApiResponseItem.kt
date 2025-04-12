package com.pp.bookxpert.models

data class ApiResponseItem(
    val id: String,
    val name: String,
    val data: Map<String, Any?>? = null
)
