package com.siputzx.app.ui.navigation

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.siputzx.app.ui.screens.*
import com.siputzx.app.ui.theme.*
import com.siputzx.app.viewmodel.MainViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Home : Screen("home", "Home", Icons.Filled.Home)
    data object Categories : Screen("categories", "Categories", Icons.Filled.Apps)
    data object Endpoints : Screen("endpoints/{categoryId}", "Endpoints", Icons.Filled.Api)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(Screen.Home.route, Screen.Categories.route)

    Scaffold(
        containerColor = PrimaryDark,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = SurfaceCard,
                    contentColor = TextPrimary,
                    tonalElevation = 0.dp,
                ) {
                    NavigationBarItem(
                        icon = { Icon(Screen.Home.icon, contentDescription = Screen.Home.title) },
                        label = { Text(Screen.Home.title) },
                        selected = currentRoute == Screen.Home.route,
                        onClick = {
                            if (currentRoute != Screen.Home.route) {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Home.route) { inclusive = true }
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Accent,
                            selectedTextColor = Accent,
                            indicatorColor = Accent.copy(alpha = 0.15f),
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted,
                        )
                    )
                    NavigationBarItem(
                        icon = { Icon(Screen.Categories.icon, contentDescription = Screen.Categories.title) },
                        label = { Text(Screen.Categories.title) },
                        selected = currentRoute == Screen.Categories.route,
                        onClick = {
                            if (currentRoute != Screen.Categories.route) {
                                navController.navigate(Screen.Categories.route) {
                                    popUpTo(Screen.Home.route) { inclusive = false }
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Accent,
                            selectedTextColor = Accent,
                            indicatorColor = Accent.copy(alpha = 0.15f),
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted,
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onCategoryClick = { category ->
                        viewModel.selectCategory(category)
                        navController.navigate("endpoints/${category.tag}")
                    }
                )
            }
            composable(Screen.Categories.route) {
                CategoriesScreen(
                    viewModel = viewModel,
                    onCategoryClick = { category ->
                        viewModel.selectCategory(category)
                        navController.navigate("endpoints/${category.tag}")
                    }
                )
            }
            composable("endpoints/{categoryId}") { backStackEntry ->
                val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
                EndpointListScreen(
                    categoryId = categoryId,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
