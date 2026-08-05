package com.siputzx.app.data.repository

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.siputzx.app.data.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody

class ApiRepository {
    private val api = RetrofitClient.apiService
    private val gson = Gson()

    suspend fun callEndpoint(path: String, params: Map<String, String>): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response: ResponseBody = api.callGet(path, params)
                val raw = response.string()
                // Pretty-print
                val pretty = try {
                    val je = JsonParser.parseString(raw)
                    Gson().toJson(je)
                } catch (_: Exception) {
                    raw
                }
                Result.success(pretty)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
