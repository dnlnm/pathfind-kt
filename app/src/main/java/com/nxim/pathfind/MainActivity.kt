package com.nxim.pathfind

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nxim.pathfind.data.api.ApiClient
import com.nxim.pathfind.data.repository.AuthRepository
import com.nxim.pathfind.data.repository.dataStore
import com.nxim.pathfind.ui.auth.AuthViewModel
import com.nxim.pathfind.ui.auth.SetupScreen
import com.nxim.pathfind.ui.main.BookmarkViewModel
import com.nxim.pathfind.ui.main.MainScreen
import com.nxim.pathfind.ui.theme.PathFindTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        authRepository = AuthRepository(applicationContext)

        setContent {
            PathFindTheme {
                val authState by authRepository.authState.collectAsState(initial = null)

                if (authState == null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (!authState!!.isAuthenticated) {
                    val authViewModel: AuthViewModel = viewModel(factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            @Suppress("UNCHECKED_CAST")
                            return AuthViewModel(authRepository) as T
                        }
                    })
                    SetupScreen(viewModel = authViewModel)
                } else {
                    // Authenticated
                    ApiClient.configure(authState!!.serverUrl, authState!!.apiToken)
                    
                    var sharedUrl by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<String?>(null) }
                    
                    if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
                        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
                        if (!sharedText.isNullOrBlank()) {
                            sharedUrl = sharedText
                        }
                        // Clear intent to avoid triggering again on config changes
                        intent.action = Intent.ACTION_MAIN
                    }

                    Box(modifier = Modifier.fillMaxSize()) {
                        val bookmarkViewModel: BookmarkViewModel = viewModel()
                        MainScreen(
                            bookmarkViewModel = bookmarkViewModel,
                            onNavigateOut = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    authRepository.clearCredentials()
                                }
                            }
                        )
                        
                        // Show dialog if we launched with a shared URL
                        if (sharedUrl != null) {
                            com.nxim.pathfind.ui.bookmarks.AddBookmarkDialog(
                                initialUrl = sharedUrl!!,
                                onDismiss = { sharedUrl = null },
                                onBookmarkAdded = {
                                    sharedUrl = null
                                    bookmarkViewModel.loadBookmarks(reset = true)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}