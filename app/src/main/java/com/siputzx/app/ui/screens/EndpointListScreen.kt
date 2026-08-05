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
import com.siputzx.app.data.model.*
import com.siputzx.app.ui.theme.*
import com.siputzx.app.viewmodel.EndpointResult
import com.siputzx.app.viewmodel.MainViewModel

@Composable
fun EndpointListScreen(
    categoryId: String,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val cat = viewModel.categories.value.find { it.tag == categoryId }
    if (cat == null) { onBack(); return }

    val catColor = CategoryColors[cat.tag.lowercase()] ?: Accent
    val results by viewModel.endpointResult.collectAsState()
    var selected by remember { mutableStateOf<EndpointInfo?>(null) }
    var params by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    if (selected != null) {
        EndpointDetail(
            ep = selected!!,
            color = catColor,
            result = results[selected!!.id],
            params = params,
            onParam = { k, v -> params = params + (k to v) },
            onExec = { viewModel.executeEndpoint(selected!!, params) },
            onClose = {
                viewModel.clearResult(selected!!.id)
                selected = null
                params = emptyMap()
            }
        )
        return
    }

    Column(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxWidth().background(Brush.horizontalGradient(listOf(catColor, catColor.copy(alpha = 0.7f)))).padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onBack) { Icon(Icons.Filled.ArrowBack, null, tint = TextPrimary) }
                Spacer(Modifier.width(8.dp))
                Column {
                    Text(cat.tag, style = MaterialTheme.typography.headlineLarge, color = TextPrimary, fontWeight = FontWeight.Bold)
                    Text("${cat.count} endpoints", color = TextPrimary.copy(alpha = 0.8f), fontSize = 13.sp)
                }
            }
        }

        LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(cat.endpoints) { ep ->
                val r = results[ep.id]
                Card(
                    Modifier.fillMaxWidth().clickable {
                        selected = ep
                        params = ep.params.associate { it.name to (it.example?.toString() ?: it.schema?.example?.toString() ?: "") }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard)
                ) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.clip(RoundedCornerShape(6.dp)).background(catColor.copy(alpha = 0.15f)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                            Text("GET", color = catColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text(ep.summary, color = TextPrimary, fontWeight = FontWeight.Medium, fontSize = 14.sp, maxLines = 1)
                            Text(ep.path, color = TextMuted, fontSize = 11.sp, fontFamily = FontFamily.Monospace, maxLines = 1)
                        }
                        when {
                            r?.loading == true -> CircularProgressIndicator(Modifier.size(18.dp), color = catColor, strokeWidth = 2.dp)
                            r?.success == true -> Icon(Icons.Filled.CheckCircle, null, tint = Green, modifier = Modifier.size(18.dp))
                            r?.error != null -> Icon(Icons.Filled.Error, null, tint = Orange, modifier = Modifier.size(18.dp))
                            else -> Icon(Icons.Filled.PlayArrow, null, tint = catColor, modifier = Modifier.size(22.dp))
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun EndpointDetail(
    ep: EndpointInfo,
    color: androidx.compose.ui.graphics.Color,
    result: EndpointResult?,
    params: Map<String, String>,
    onParam: (String, String) -> Unit,
    onExec: () -> Unit,
    onClose: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxWidth().background(Brush.horizontalGradient(listOf(color, color.copy(alpha = 0.7f)))).padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClose) { Icon(Icons.Filled.ArrowBack, null, tint = TextPrimary) }
                Column(Modifier.weight(1f)) {
                    Text(ep.summary, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(ep.path, color = TextPrimary.copy(alpha = 0.7f), fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                }
            }
        }

        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp)) {
            if (ep.params.isNotEmpty()) {
                Text("Parameters", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Spacer(Modifier.height(10.dp))
                ep.params.forEach { p ->
                    OutlinedTextField(
                        value = params[p.name] ?: "",
                        onValueChange = { onParam(p.name, it) },
                        label = { Text("${p.name}${if (p.required) " *" else ""}", color = TextSecondary) },
                        placeholder = { Text(p.description ?: "", color = TextMuted) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = color, unfocusedBorderColor = SurfaceCardLight,
                            focusedContainerColor = SurfaceInput, unfocusedContainerColor = SurfaceInput, cursorColor = color
                        ),
                        singleLine = true
                    )
                    Spacer(Modifier.height(8.dp))
                }
            } else {
                Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = SurfaceCard)) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Info, null, tint = Cyan); Spacer(Modifier.width(10.dp)); Text("No parameters required", color = TextSecondary)
                    }
                }
                Spacer(Modifier.height(14.dp))
            }

            Button(
                onClick = onExec, modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = result?.loading != true,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = color)
            ) {
                if (result?.loading == true) {
                    CircularProgressIndicator(Modifier.size(18.dp), color = TextPrimary, strokeWidth = 2.dp)
                    Spacer(Modifier.width(8.dp)); Text("Loading...", color = TextPrimary)
                } else {
                    Icon(Icons.Filled.PlayArrow, null, tint = TextPrimary); Spacer(Modifier.width(8.dp))
                    Text("Execute", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                }
            }

            if (result?.body != null || result?.error != null) {
                Spacer(Modifier.height(18.dp))
                Text("Response", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Spacer(Modifier.height(10.dp))
                Card(
                    Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = if (result.success) Green.copy(alpha = 0.08f) else Orange.copy(alpha = 0.08f))
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Box(Modifier.clip(RoundedCornerShape(6.dp)).background(if (result.success) Green.copy(alpha = 0.2f) else Orange.copy(alpha = 0.2f)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                            Text(if (result.success) "200 OK" else "ERROR", color = if (result.success) Green else Orange, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.height(10.dp))
                        SelectionContainer {
                            Text(
                                text = result.body ?: result.error ?: "",
                                color = if (result.success) TextPrimary else Orange,
                                fontSize = 12.sp, fontFamily = FontFamily.Monospace, lineHeight = 18.sp,
                                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(SurfaceCard.copy(alpha = 0.5f)).padding(12.dp)
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
