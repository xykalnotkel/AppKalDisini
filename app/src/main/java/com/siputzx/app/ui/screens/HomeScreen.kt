package com.siputzx.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.siputzx.app.data.model.CategoryGroup
import com.siputzx.app.ui.theme.*
import com.siputzx.app.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onCategoryClick: (CategoryGroup) -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isLoadingSpec by viewModel.isLoadingSpec.collectAsState()
    val specError by viewModel.specError.collectAsState()
    val categories by viewModel.categories.collectAsState()

    val displayCategories = viewModel.filteredCategories
    val totalEndpoints = categories.sumOf { it.count }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(Primary, PrimaryDark)))
                .padding(24.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(48.dp).clip(CircleShape)
                            .background(Brush.linearGradient(listOf(Accent, Cyan))),
                        contentAlignment = Alignment.Center
                    ) { Icon(Icons.Filled.Api, "Logo", tint = androidx.compose.ui.graphics.Color.White, modifier = Modifier.size(28.dp)) }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text("SIPUTZX API", style = MaterialTheme.typography.headlineLarge, color = TextPrimary, fontWeight = FontWeight.Bold)
                        Text("Free Public REST API · No API Key", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                    }
                }
                Spacer(Modifier.height(20.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = { Text("Search endpoints...", color = TextMuted) },
                    leadingIcon = { Icon(Icons.Filled.Search, "Search", tint = Accent) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Accent, unfocusedBorderColor = SurfaceCardLight,
                        focusedContainerColor = SurfaceCard, unfocusedContainerColor = SurfaceCard,
                        cursorColor = Accent
                    ),
                    singleLine = true
                )
            }
        }

        when {
            isLoadingSpec -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Accent)
                        Spacer(Modifier.height(16.dp))
                        Text("Loading API spec...", color = TextSecondary)
                    }
                }
            }
            specError != null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(32.dp)) {
                        Icon(Icons.Filled.CloudOff, null, tint = Orange, modifier = Modifier.size(64.dp))
                        Spacer(Modifier.height(16.dp))
                        Text("Gagal connect API", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(specError ?: "", color = TextMuted, fontSize = 13.sp)
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadSpec() }, colors = ButtonDefaults.buttonColors(containerColor = Accent)) {
                            Text("Retry")
                        }
                    }
                }
            }
            else -> {
                // Stats
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard("${categories.size}", "Categories", Accent, Modifier.weight(1f))
                    StatCard("$totalEndpoints", "Endpoints", Cyan, Modifier.weight(1f))
                    StatCard("FREE", "No API Key", Pink, Modifier.weight(1f))
                }

                Text("Quick Access", style = MaterialTheme.typography.titleLarge, color = TextPrimary,
                    fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp))

                LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(categories.take(8)) { cat ->
                        val color = CategoryColors[cat.tag.lowercase()] ?: Accent
                        Surface(
                            modifier = Modifier.clickable { onCategoryClick(cat) },
                            shape = RoundedCornerShape(24.dp),
                            color = color.copy(alpha = 0.15f)
                        ) {
                            Row(Modifier.padding(horizontal = 14.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(cat.tag, color = color, fontWeight = FontWeight.Medium, fontSize = 13.sp)
                                Spacer(Modifier.width(4.dp))
                                Text("${cat.count}", color = color.copy(alpha = 0.6f), fontSize = 11.sp)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                Text("All Categories", style = MaterialTheme.typography.titleLarge, color = TextPrimary,
                    fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp))

                LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(displayCategories) { cat -> CategoryCard(cat, onClick = { onCategoryClick(cat) }) }
                    item { Spacer(Modifier.height(16.dp)) }
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, color: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
    Card(modifier = modifier, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = SurfaceCard)) {
        Column(Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(label, color = TextSecondary, fontSize = 11.sp)
        }
    }
}

@Composable
fun CategoryCard(cat: CategoryGroup, onClick: () -> Unit) {
    val color = CategoryColors[cat.tag.lowercase()] ?: Accent
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(48.dp).clip(RoundedCornerShape(14.dp))
                    .background(Brush.linearGradient(listOf(color.copy(alpha = 0.3f), color.copy(alpha = 0.08f)))),
                contentAlignment = Alignment.Center
            ) { Text(cat.tag.take(2).uppercase(), color = color, fontWeight = FontWeight.Bold, fontSize = 16.sp) }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(cat.tag, color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Text("${cat.count} endpoint${if (cat.count > 1) "s" else ""}", color = TextSecondary, fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${cat.count}", color = color, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("API", color = TextMuted, fontSize = 10.sp)
            }
            Spacer(Modifier.width(4.dp))
            Icon(Icons.Filled.ChevronRight, null, tint = TextMuted, modifier = Modifier.size(20.dp))
        }
    }
}
