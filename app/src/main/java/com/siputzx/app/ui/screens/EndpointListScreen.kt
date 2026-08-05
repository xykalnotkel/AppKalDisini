package com.siputzx.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.siputzx.app.data.model.*
import com.siputzx.app.ui.theme.*
import com.siputzx.app.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndpointListScreen(
    categoryId: String,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val category = viewModel.categories.find { it.id == categoryId } ?: return
    val catColor = CategoryColors[category.id] ?: Accent
    val endpointStates by viewModel.endpointStates.collectAsState()
    var selectedEndpoint by remember { mutableStateOf<EndpointDef?>(null) }
    var paramValues by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    if (selectedEndpoint != null) {
        EndpointDetailSheet(
            endpoint = selectedEndpoint!!,
            categoryColor = catColor,
            state = endpointStates[selectedEndpoint!!.id],
            paramValues = paramValues,
            onParamChange = { key, value ->
                paramValues = paramValues + (key to value)
            },
            onExecute = {
                viewModel.callEndpoint(selectedEndpoint!!.id, paramValues)
            },
            onClose = {
                viewModel.clearEndpointState(selectedEndpoint!!.id)
                selectedEndpoint = null
                paramValues = emptyMap()
            }
        )
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Category Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(listOf(catColor, catColor.copy(alpha = 0.7f)))
                )
                .padding(24.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, "Back", tint = TextPrimary)
                    }
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text(
                            category.name,
                            style = MaterialTheme.typography.headlineLarge,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "${category.endpointCount} endpoints available",
                            color = TextPrimary.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(category.endpoints) { endpoint ->
                val state = endpointStates[endpoint.id]
                EndpointCard(
                    endpoint = endpoint,
                    categoryColor = catColor,
                    isLoading = state?.isLoading == true,
                    hasResponse = state?.response != null,
                    hasError = state?.error != null,
                    onClick = {
                        selectedEndpoint = endpoint
                        paramValues = endpoint.params.associate { it.key to it.defaultValue }
                    }
                )
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun EndpointCard(
    endpoint: EndpointDef,
    categoryColor: androidx.compose.ui.graphics.Color,
    isLoading: Boolean,
    hasResponse: Boolean,
    hasError: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Method badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(categoryColor.copy(alpha = 0.15f))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text("GET", color = categoryColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(endpoint.name, color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Text(
                    endpoint.path,
                    color = TextMuted,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 1
                )
            }

            when {
                isLoading -> CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = categoryColor,
                    strokeWidth = 2.dp
                )
                hasResponse -> Icon(Icons.Filled.CheckCircle, null, tint = Green, modifier = Modifier.size(20.dp))
                hasError -> Icon(Icons.Filled.Error, null, tint = Orange, modifier = Modifier.size(20.dp))
                else -> Icon(Icons.Filled.PlayArrow, null, tint = categoryColor, modifier = Modifier.size(24.dp))
            }
        }
    }
}

@Composable
fun EndpointDetailSheet(
    endpoint: EndpointDef,
    categoryColor: androidx.compose.ui.graphics.Color,
    state: com.siputzx.app.viewmodel.EndpointUiState?,
    paramValues: Map<String, String>,
    onParamChange: (String, String) -> Unit,
    onExecute: () -> Unit,
    onClose: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(listOf(categoryColor, categoryColor.copy(alpha = 0.7f))))
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onClose) {
                    Icon(Icons.Filled.ArrowBack, "Close", tint = TextPrimary)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(endpoint.name, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(endpoint.path, color = TextPrimary.copy(alpha = 0.7f), fontSize = 13.sp, fontFamily = FontFamily.Monospace)
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Description
            Text(endpoint.description, color = TextSecondary, fontSize = 14.sp)
            Spacer(Modifier.height(20.dp))

            // Parameters
            if (endpoint.params.isNotEmpty()) {
                Text("Parameters", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Spacer(Modifier.height(12.dp))

                endpoint.params.forEach { param ->
                    OutlinedTextField(
                        value = paramValues[param.key] ?: param.defaultValue,
                        onValueChange = { onParamChange(param.key, it) },
                        label = { Text(param.label, color = TextSecondary) },
                        placeholder = { Text(param.placeholder, color = TextMuted) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = categoryColor,
                            unfocusedBorderColor = SurfaceCardLight,
                            focusedContainerColor = SurfaceInput,
                            unfocusedContainerColor = SurfaceInput,
                            cursorColor = categoryColor,
                        ),
                        singleLine = true,
                    )
                    Spacer(Modifier.height(8.dp))
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Info, null, tint = Cyan)
                        Spacer(Modifier.width(12.dp))
                        Text("No parameters required", color = TextSecondary)
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            // Execute button
            Button(
                onClick = onExecute,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = state?.isLoading != true,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = categoryColor)
            ) {
                if (state?.isLoading == true) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = TextPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Loading...", color = TextPrimary)
                } else {
                    Icon(Icons.Filled.PlayArrow, null, tint = TextPrimary)
                    Spacer(Modifier.width(8.dp))
                    Text("Execute", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(Modifier.height(20.dp))

            // Response
            if (state?.response != null || state?.error != null) {
                Text("Response", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Spacer(Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (state.isSuccess) Green.copy(alpha = 0.1f) else Orange.copy(alpha = 0.1f)
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Status badge
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        if (state.isSuccess) Green.copy(alpha = 0.2f) else Orange.copy(alpha = 0.2f)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    if (state.isSuccess) "200 OK" else "ERROR",
                                    color = if (state.isSuccess) Green else Orange,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(Modifier.height(12.dp))

                        // JSON Response
                        SelectionContainer {
                            Text(
                                text = state.response ?: state.error ?: "",
                                color = if (state.isSuccess) TextPrimary else Orange,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                lineHeight = 18.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SurfaceCard.copy(alpha = 0.5f))
                                    .padding(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
