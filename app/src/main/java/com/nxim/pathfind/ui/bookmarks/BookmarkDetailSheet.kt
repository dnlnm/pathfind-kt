package com.nxim.pathfind.ui.bookmarks

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nxim.pathfind.data.model.Bookmark
import com.nxim.pathfind.ui.main.BookmarkViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkDetailSheet(
    bookmark: Bookmark,
    viewModel: BookmarkViewModel,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = bookmark.title ?: bookmark.domain,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = bookmark.url,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (!bookmark.description.isNullOrEmpty()) {
                Text(
                    text = bookmark.description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { 
                    viewModel.toggleReadLater(bookmark)
                    onDismiss()
                }) {
                    Text(if (bookmark.isReadLater) "Remove Read Later" else "Read Later")
                }
                
                Button(onClick = { 
                    viewModel.toggleArchive(bookmark)
                    onDismiss()
                }) {
                    Text(if (bookmark.isArchived) "Unarchive" else "Archive")
                }
            }

            Button(
                onClick = { 
                    viewModel.deleteBookmark(bookmark.id)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Delete Bookmark")
            }
        }
    }
}
