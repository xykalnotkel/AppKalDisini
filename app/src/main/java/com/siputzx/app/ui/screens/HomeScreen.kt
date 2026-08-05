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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.siputzx.app.data.model.Category
import com.siputzx.app.ui.theme.*
import com.siputzx.app.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onCategoryClick: (Category) -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Primary, PrimaryDark)
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(Accent, Cyan))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Api, "Logo", tint = Color.White, modifier = Modifier.size(28.dp))
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            "SIPUTZX API",
                            style = MaterialTheme.typography.headlineLarge,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "200+ Free Public API Endpoints",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                // Search bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = { Text("Search endpoints...", color = TextMuted) },
                    leadingIcon = { Icon(Icons.Filled.Search, "Search", tint = Accent) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Accent,
                        unfocusedBorderColor = SurfaceCardLight,
                        focusedContainerColor = SurfaceCard,
                        unfocusedContainerColor = SurfaceCard,
                        cursorColor = Accent,
                    ),
                    singleLine = true,
                )
            }
        }

        // Quick Stats
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard("14", "Categories", Accent, Modifier.weight(1f))
            StatCard("200+", "Endpoints", Cyan, Modifier.weight(1f))
            StatCard("FREE", "No API Key", Pink, Modifier.weight(1f))
        }

        // Quick Categories
        Text(
            "Quick Access",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(viewModel.categories.take(8)) { category ->
                QuickCategoryChip(category, onClick = { onCategoryClick(category) })
            }
        }

        Spacer(Modifier.height(12.dp))

        // All Categories Grid
        Text(
            "All Categories",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        val displayCategories = viewModel.filteredCategories
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(displayCategories) { category ->
                CategoryCard(category, onClick = { onCategoryClick(category) })
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Text(label, color = TextSecondary, fontSize = 12.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun QuickCategoryChip(category: Category, onClick: () -> Unit) {
    val catColor = CategoryColors[category.id] ?: Accent
    Surface(
        modifier = Modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        color = catColor.copy(alpha = 0.15f),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(category.icon, null, tint = catColor, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text(category.name, color = catColor, fontWeight = FontWeight.Medium, fontSize = 14.sp)
        }
    }
}

@Composable
fun CategoryCard(category: Category, onClick: () -> Unit) {
    val catColor = CategoryColors[category.id] ?: Accent
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon container
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(catColor.copy(alpha = 0.3f), catColor.copy(alpha = 0.1f))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(category.icon, null, tint = catColor, modifier = Modifier.size(28.dp))
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(category.name, color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(category.description, color = TextSecondary, fontSize = 13.sp)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "${category.endpointCount}",
                    color = catColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text("APIs", color = TextMuted, fontSize = 10.sp)
            }

            Spacer(Modifier.width(4.dp))
            Icon(Icons.Filled.ChevronRight, null, tint = TextMuted, modifier = Modifier.size(20.dp))
        }
    }
}
