package com.siputzx.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.siputzx.app.data.api.ApiResponse
import com.siputzx.app.data.model.Category
import com.siputzx.app.data.model.allCategories
import com.siputzx.app.data.repository.ApiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EndpointUiState(
    val isLoading: Boolean = false,
    val response: String? = null,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class MainViewModel : ViewModel() {
    private val repository = ApiRepository()
    private val gson = GsonBuilder().setPrettyPrinting().create()

    val categories = allCategories

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory.asStateFlow()

    private val _endpointStates = MutableStateFlow<Map<String, EndpointUiState>>(emptyMap())
    val endpointStates: StateFlow<Map<String, EndpointUiState>> = _endpointStates.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun selectCategory(category: Category?) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    val filteredCategories: List<Category>
        get() {
            val q = _searchQuery.value.lowercase()
            if (q.isEmpty()) return allCategories
            return allCategories.filter {
                it.name.lowercase().contains(q) ||
                        it.description.lowercase().contains(q) ||
                        it.endpoints.any { e -> e.name.lowercase().contains(q) || e.description.lowercase().contains(q) }
            }
        }

    fun callEndpoint(endpointId: String, params: Map<String, String>) {
        viewModelScope.launch {
            _endpointStates.value = _endpointStates.value + (endpointId to EndpointUiState(isLoading = true))
            val result = repository.callEndpoint(endpointId, params)
            result.fold(
                onSuccess = { apiResponse ->
                    val prettyJson = try {
                        val jsonElement = JsonParser.parseString(gson.toJson(apiResponse))
                        gson.toJson(jsonElement)
                    } catch (e: Exception) {
                        apiResponse.toString()
                    }
                    _endpointStates.value = _endpointStates.value + (endpointId to EndpointUiState(
                        isLoading = false,
                        response = prettyJson,
                        isSuccess = true
                    ))
                },
                onFailure = { error ->
                    _endpointStates.value = _endpointStates.value + (endpointId to EndpointUiState(
                        isLoading = false,
                        error = error.message ?: "Unknown error",
                        isSuccess = false
                    ))
                }
            )
        }
    }

    fun clearEndpointState(endpointId: String) {
        _endpointStates.value = _endpointStates.value - endpointId
    }
}
