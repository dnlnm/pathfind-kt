package com.nxim.pathfind.ui.collections

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nxim.pathfind.ui.main.BookmarkViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionListScreen(
    viewModel: BookmarkViewModel,
    onCollectionSelected: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Collections") })
        }
    ) { paddingValues ->
        if (uiState.collections.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("No collections found", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(uiState.collections) { collection ->
                    ListItem(
                        headlineContent = { Text(collection.name) },
                        supportingContent = if (!collection.description.isNullOrEmpty()) {
                            { Text(collection.description) }
                        } else null,
                        leadingContent = {
                            Icon(Icons.Default.Folder, contentDescription = "Folder")
                        },
                        trailingContent = {
                            Text("${collection.bookmarkCount} links", style = MaterialTheme.typography.labelMedium)
                        },
                        modifier = Modifier.clickable {
                            viewModel.setCollectionFilter(collection.id)
                            onCollectionSelected()
                        }
                    )
                    Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                }
            }
        }
    }
}
