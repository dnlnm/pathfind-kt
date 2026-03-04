package com.nxim.pathfind.ui.bookmarks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nxim.pathfind.data.model.Bookmark
import com.nxim.pathfind.ui.main.BookmarkViewModel
import kotlinx.coroutines.launch
import android.content.Intent
import android.net.Uri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkListScreen(
    viewModel: BookmarkViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val context = LocalContext.current

    var showAddDialog by remember { mutableStateOf(false) }
    var selectedBookmark: Bookmark? by remember { mutableStateOf(null) }

    var showFilterMenu by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.activeCollection ?: uiState.activeTag?.let { "#$it" } ?: "Bookmarks") },
                actions = {
                    if (uiState.activeCollection != null || uiState.activeTag != null) {
                        TextButton(onClick = { viewModel.clearFilters() }) {
                            Text("Clear")
                        }
                    }
                    IconButton(onClick = { viewModel.toggleViewStyle() }) {
                        Icon(Icons.Default.BookmarkBorder, contentDescription = "Toggle View")
                    }
                    Box {
                        IconButton(onClick = { showFilterMenu = true }) {
                            Icon(Icons.Default.FilterAlt, contentDescription = "Filter")
                        }
                        DropdownMenu(expanded = showFilterMenu, onDismissRequest = { showFilterMenu = false }) {
                            DropdownMenuItem(text = { Text("All") }, onClick = { viewModel.setFilter("all"); showFilterMenu = false })
                            DropdownMenuItem(text = { Text("Read Later") }, onClick = { viewModel.setFilter("readlater"); showFilterMenu = false })
                            DropdownMenuItem(text = { Text("Archived") }, onClick = { viewModel.setFilter("archived"); showFilterMenu = false })
                        }
                    }
                    Box {
                        IconButton(onClick = { showSortMenu = true }) {
                            Icon(Icons.Default.Sort, contentDescription = "Sort")
                        }
                        DropdownMenu(expanded = showSortMenu, onDismissRequest = { showSortMenu = false }) {
                            DropdownMenuItem(text = { Text("Newest") }, onClick = { viewModel.setSort("newest"); showSortMenu = false })
                            DropdownMenuItem(text = { Text("Oldest") }, onClick = { viewModel.setSort("oldest"); showSortMenu = false })
                            DropdownMenuItem(text = { Text("Title A-Z") }, onClick = { viewModel.setSort("title_asc"); showSortMenu = false })
                            DropdownMenuItem(text = { Text("Title Z-A") }, onClick = { viewModel.setSort("title_desc"); showSortMenu = false })
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            if (uiState.isLoading && uiState.bookmarks.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.bookmarks.isEmpty()) {
                Text(
                    "No bookmarks found",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(uiState.bookmarks, key = { it.id }) { bookmark ->
                        BookmarkCard(
                            bookmark = bookmark,
                            isCompact = !uiState.isCardView,
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(bookmark.url))
                                context.startActivity(intent)
                            },
                            onLongClick = { selectedBookmark = bookmark }
                        )

                        // Pagination check
                        if (bookmark.id == uiState.bookmarks.last().id && !uiState.isLoadingMore && uiState.page < uiState.totalPages) {
                            LaunchedEffect(bookmark.id) {
                                viewModel.loadBookmarks(reset = false)
                            }
                        }
                    }

                    if (uiState.isLoadingMore) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddBookmarkDialog(
            onDismiss = { showAddDialog = false },
            onBookmarkAdded = { viewModel.loadBookmarks(reset = true) }
        )
    }

    selectedBookmark?.let { bookmark ->
        BookmarkDetailSheet(
            bookmark = bookmark,
            viewModel = viewModel,
            onDismiss = { selectedBookmark = null }
        )
    }
}
