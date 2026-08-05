package com.siputzx.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.siputzx.app.data.api.RetrofitClient
import com.siputzx.app.data.model.*
import com.siputzx.app.data.repository.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val repository = ApiRepository()
    private val gson = Gson()

    // OpenAPI loading
    private val _isLoadingSpec = MutableStateFlow(false)
    val isLoadingSpec: StateFlow<Boolean> = _isLoadingSpec.asStateFlow()

    private val _specError = MutableStateFlow<String?>(null)
    val specError: StateFlow<String?> = _specError.asStateFlow()

    private val _categories = MutableStateFlow<List<CategoryGroup>>(emptyList())
    val categories: StateFlow<List<CategoryGroup>> = _categories.asStateFlow()

    // Search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Selected category
    private val _selectedCategory = MutableStateFlow<CategoryGroup?>(null)
    val selectedCategory: StateFlow<CategoryGroup?> = _selectedCategory.asStateFlow()

    // Endpoint execution
    private val _endpointResult = MutableStateFlow<Map<String, EndpointResult>>(emptyMap())
    val endpointResult: StateFlow<Map<String, EndpointResult>> = _endpointResult.asStateFlow()

    init {
        loadSpec()
    }

    fun loadSpec() {
        viewModelScope.launch {
            _isLoadingSpec.value = true
            _specError.value = null
            try {
                val json = with(kotlinx.coroutines.Dispatchers.IO) {
                    java.net.URL(RetrofitClient.OPENAPI_URL).readText()
                }
                val spec = gson.fromJson(json, OpenApiSpec::class.java)

                val grouped = mutableMapOf<String, MutableList<EndpointInfo>>()
                for ((path, methods) in spec.paths) {
                    for ((method, item) in methods) {
                        if (method.lowercase() != "get") continue
                        val tag = item.tags.firstOrNull() ?: "Other"
                        val id = path.removePrefix("/").replace("/", "-")
                        grouped.getOrPut(tag) { mutableListOf() }.add(
                            EndpointInfo(
                                id = id,
                                path = path,
                                summary = item.summary ?: path,
                                tag = tag,
                                params = item.parameters.filter { it.location == "query" }
                            )
                        )
                    }
                }
                _categories.value = grouped.map { (tag, eps) ->
                    CategoryGroup(tag = tag, endpoints = eps, count = eps.size)
                }.sortedBy { it.tag }
            } catch (e: Exception) {
                _specError.value = e.message ?: "Gagal load API spec"
            } finally {
                _isLoadingSpec.value = false
            }
        }
    }

    fun setSearchQuery(q: String) {
        _searchQuery.value = q
    }

    val filteredCategories: List<CategoryGroup>
        get() {
            val q = _searchQuery.value.lowercase().trim()
            if (q.isEmpty()) return _categories.value
            return _categories.value.filter { cat ->
                cat.tag.lowercase().contains(q) ||
                        cat.endpoints.any { it.summary.lowercase().contains(q) || it.path.lowercase().contains(q) }
            }
        }

    fun selectCategory(cat: CategoryGroup) {
        _selectedCategory.value = cat
    }

    fun executeEndpoint(endpoint: EndpointInfo, params: Map<String, String>) {
        viewModelScope.launch {
            _endpointResult.value = _endpointResult.value + (endpoint.id to EndpointResult(loading = true))
            val result = repository.callEndpoint(endpoint.path, params)
            result.fold(
                onSuccess = { json ->
                    _endpointResult.value = _endpointResult.value + (endpoint.id to EndpointResult(
                        loading = false, success = true, body = json
                    ))
                },
                onFailure = { e ->
                    _endpointResult.value = _endpointResult.value + (endpoint.id to EndpointResult(
                        loading = false, success = false, error = e.message ?: "Unknown error"
                    ))
                }
            )
        }
    }

    fun clearResult(id: String) {
        _endpointResult.value = _endpointResult.value - id
    }
}

data class EndpointResult(
    val loading: Boolean = false,
    val success: Boolean = false,
    val body: String? = null,
    val error: String? = null
)
