package com.nxim.pathfind.ui.bookmarks

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nxim.pathfind.data.api.ApiClient
import com.nxim.pathfind.data.model.BookmarkCreateRequest
import kotlinx.coroutines.launch

@Composable
fun AddBookmarkDialog(
    initialUrl: String = "",
    onDismiss: () -> Unit,
    onBookmarkAdded: () -> Unit
) {
    var url by remember { mutableStateOf(initialUrl) }
    var title by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Bookmark") },
        text = {
            Column(
                modifier = Modifier.padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("URL") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title (Optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (url.isNotBlank()) {
                        isSaving = true
                        coroutineScope.launch {
                            try {
                                ApiClient.api.createBookmark(
                                    BookmarkCreateRequest(
                                        url = url,
                                        title = title.takeIf { it.isNotBlank() }
                                    )
                                )
                                onBookmarkAdded()
                                onDismiss()
                            } catch (e: Exception) {
                                isSaving = false
                            }
                        }
                    }
                },
                enabled = url.isNotBlank() && !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Save")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isSaving) {
                Text("Cancel")
            }
        }
    )
}
