package com.nxim.pathfind.ui.search

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nxim.pathfind.data.model.Bookmark
import com.nxim.pathfind.ui.bookmarks.BookmarkCard
import com.nxim.pathfind.ui.bookmarks.BookmarkDetailSheet
import com.nxim.pathfind.ui.main.BookmarkViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: BookmarkViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val context = LocalContext.current

    var selectedBookmark: Bookmark? by remember { mutableStateOf(null) }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(title = { Text("Search") })
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = { Text("Search bookmarks...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search icon") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    singleLine = true
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            if (uiState.searchQuery.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(52.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text("Search Bookmarks", style = MaterialTheme.typography.titleMedium)
                    Text("Type to search by title, URL, or description", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else if (uiState.isSearchLoading && uiState.searchResults.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.searchResults.isEmpty() && !uiState.isSearchLoading) {
                Text(
                    "No results for \"${uiState.searchQuery}\"",
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
                    items(uiState.searchResults, key = { it.id }) { bookmark ->
                        BookmarkCard(
                            bookmark = bookmark,
                            isCompact = true,
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(bookmark.url))
                                context.startActivity(intent)
                            },
                            onLongClick = { selectedBookmark = bookmark }
                        )

                        if (bookmark.id == uiState.searchResults.last().id && !uiState.isSearchLoadingMore && uiState.searchPage < uiState.searchTotalPages) {
                            LaunchedEffect(bookmark.id) {
                                viewModel.searchBookmarks(reset = false)
                            }
                        }
                    }

                    if (uiState.isSearchLoadingMore) {
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

    selectedBookmark?.let { bookmark ->
        BookmarkDetailSheet(
            bookmark = bookmark,
            viewModel = viewModel,
            onDismiss = { selectedBookmark = null }
        )
    }
}
