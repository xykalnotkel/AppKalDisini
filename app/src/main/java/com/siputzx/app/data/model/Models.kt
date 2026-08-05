package com.siputzx.app.data.model

import com.google.gson.annotations.SerializedName

// OpenAPI Spec models
data class OpenApiSpec(
    val paths: Map<String, Map<String, PathItem>> = emptyMap(),
    val tags: List<TagInfo> = emptyList()
)

data class TagInfo(val name: String)

data class PathItem(
    val summary: String? = null,
    val description: String? = null,
    val tags: List<String> = emptyList(),
    val parameters: List<ParamSpec> = emptyList()
)

data class ParamSpec(
    val name: String = "",
    @SerializedName("in") val location: String = "query",
    val required: Boolean = false,
    val description: String? = null,
    val example: Any? = null,
    val schema: ParamSchema? = null
)

data class ParamSchema(
    val type: String? = null,
    val example: Any? = null
)

// Parsed endpoint
data class EndpointInfo(
    val id: String,
    val path: String,
    val method: String = "get",
    val summary: String = "",
    val tag: String = "",
    val params: List<ParamSpec> = emptyList()
)

data class CategoryGroup(
    val tag: String,
    val endpoints: List<EndpointInfo>,
    val count: Int
)
