package com.nxim.pathfind.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nxim.pathfind.ui.bookmarks.BookmarkListScreen
import com.nxim.pathfind.ui.collections.CollectionListScreen
import com.nxim.pathfind.ui.search.SearchScreen
import com.nxim.pathfind.ui.settings.SettingsScreen
import com.nxim.pathfind.ui.tags.TagListScreen

sealed class BottomNavItem(var title: String, var icon: ImageVector, var route: String) {
    object Bookmarks : BottomNavItem("Bookmarks", Icons.Filled.Bookmarks, "bookmarks")
    object Collections : BottomNavItem("Collections", Icons.Filled.Folder, "collections")
    object Tags : BottomNavItem("Tags", Icons.Filled.Tag, "tags")
    object Search : BottomNavItem("Search", Icons.Filled.Search, "search")
    object Settings : BottomNavItem("Settings", Icons.Filled.Settings, "settings")
}

@Composable
fun MainScreen(
    bookmarkViewModel: BookmarkViewModel,
    onNavigateOut: () -> Unit
) {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem.Bookmarks,
        BottomNavItem.Collections,
        BottomNavItem.Tags,
        BottomNavItem.Search,
        BottomNavItem.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Bookmarks.route,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(BottomNavItem.Bookmarks.route) {
                    BookmarkListScreen(viewModel = bookmarkViewModel)
                }
                composable(BottomNavItem.Collections.route) {
                    CollectionListScreen(viewModel = bookmarkViewModel, onCollectionSelected = {
                        navController.navigate(BottomNavItem.Bookmarks.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    })
                }
                composable(BottomNavItem.Tags.route) {
                    TagListScreen(viewModel = bookmarkViewModel, onTagSelected = {
                        navController.navigate(BottomNavItem.Bookmarks.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    })
                }
                composable(BottomNavItem.Search.route) {
                    SearchScreen(viewModel = bookmarkViewModel)
                }
                composable(BottomNavItem.Settings.route) {
                    SettingsScreen(onDisconnect = onNavigateOut)
                }
            }
        }
    }
}
