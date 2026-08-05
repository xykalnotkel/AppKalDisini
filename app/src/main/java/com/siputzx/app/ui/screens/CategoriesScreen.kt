package com.siputzx.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.siputzx.app.data.model.Category
import com.siputzx.app.ui.theme.*
import com.siputzx.app.viewmodel.MainViewModel

@Composable
fun CategoriesScreen(
    viewModel: MainViewModel,
    onCategoryClick: (Category) -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val displayCategories = viewModel.filteredCategories

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(listOf(Accent, Cyan))
                )
                .padding(24.dp)
        ) {
            Column {
                Text(
                    "API Categories",
                    style = MaterialTheme.typography.headlineLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "${viewModel.categories.size} categories available",
                    color = TextPrimary.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
            }
        }

        // Search
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            placeholder = { Text("Filter categories...", color = TextMuted) },
            leadingIcon = { Icon(Icons.Filled.Search, "Search", tint = Accent) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.setSearchQuery("") }) {
                        Icon(Icons.Filled.Close, "Clear", tint = TextSecondary)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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

        // Categories Grid
        if (displayCategories.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.SearchOff, null, tint = TextMuted, modifier = Modifier.size(64.dp))
                    Spacer(Modifier.height(16.dp))
                    Text("No categories found", color = TextSecondary, fontSize = 16.sp)
                    Text("Try a different search term", color = TextMuted, fontSize = 13.sp)
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(displayCategories) { category ->
                    CategoryCard(category, onClick = { onCategoryClick(category) })
                }
                item { Spacer(Modifier.height(16.dp)) }
            }
        }
    }
}
