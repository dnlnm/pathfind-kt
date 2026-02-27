package com.nxim.pathfind.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nxim.pathfind.data.api.ApiClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onDisconnect: () -> Unit
) {
    var showDisconnectConfirm by remember { mutableStateOf(false) }

    if (showDisconnectConfirm) {
        AlertDialog(
            onDismissRequest = { showDisconnectConfirm = false },
            title = { Text("Disconnect?") },
            text = { Text("You will need to re-enter your server URL and API token to reconnect.") },
            confirmButton = {
                TextButton(onClick = {
                    showDisconnectConfirm = false
                    onDisconnect()
                }, colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)) {
                    Text("Disconnect")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDisconnectConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Text(
                    text = "Connection",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
                )

                ListItem(
                    headlineContent = { Text("Connected Server") },
                    supportingContent = { Text(ApiClient.serverUrl) },
                    leadingContent = { Icon(Icons.Default.Shield, contentDescription = null) }
                )
                
                ListItem(
                    headlineContent = { Text("API Token") },
                    supportingContent = { Text(maskToken(ApiClient.apiToken)) },
                    leadingContent = { Icon(Icons.Default.Shield, contentDescription = null) }
                )
                
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                ListItem(
                    headlineContent = { Text("Disconnect", color = MaterialTheme.colorScheme.error) },
                    leadingContent = { Icon(Icons.Default.Logout, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                    modifier = Modifier.clickable { showDisconnectConfirm = true }
                )
                
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                
                Text(
                    text = "About",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                )

                ListItem(
                    headlineContent = { Text("App Version") },
                    trailingContent = { Text("1.0.0") },
                    leadingContent = { Icon(Icons.Default.Info, contentDescription = null) }
                )
            }
        }
    }
}

fun maskToken(token: String): String {
    if (token.length <= 8) return "••••"
    return token.take(4) + "••••" + token.takeLast(4)
}
