package com.nxim.pathfind.ui.tags

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tag
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
fun TagListScreen(
    viewModel: BookmarkViewModel,
    onTagSelected: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Tags") })
        }
    ) { paddingValues ->
        if (uiState.tags.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("No tags found", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(uiState.tags) { tag ->
                    ListItem(
                        headlineContent = { Text(tag.name) },
                        leadingContent = {
                            Icon(Icons.Default.Tag, contentDescription = "Tag")
                        },
                        trailingContent = {
                            Text("${tag.bookmarkCount} links", style = MaterialTheme.typography.labelMedium)
                        },
                        modifier = Modifier.clickable {
                            viewModel.setTagFilter(tag.name)
                            onTagSelected()
                        }
                    )
                    Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                }
            }
        }
    }
}
