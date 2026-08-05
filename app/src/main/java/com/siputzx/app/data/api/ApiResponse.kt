package com.siputzx.app.data.api

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("status") val status: Boolean? = null,
    @SerializedName("code") val code: Int? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: Any? = null,
    @SerializedName("result") val result: Any? = null,
    @SerializedName("url") val url: String? = null,
    @SerializedName("image") val image: String? = null,
)
