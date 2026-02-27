package com.nxim.pathfind.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nxim.pathfind.data.api.ApiClient
import com.nxim.pathfind.data.model.Bookmark
import com.nxim.pathfind.data.model.BookmarkUpdateRequest
import com.nxim.pathfind.data.model.Collection
import com.nxim.pathfind.data.model.Tag
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BookmarkUiState(
    val bookmarks: List<Bookmark> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val page: Int = 1,
    val totalPages: Int = 1,
    
    val filter: String = "all", // "all", "readlater", "archived"
    val sort: String = "newest", // "newest", "oldest", "title_asc", "title_desc"
    val searchQuery: String = "",
    val activeTag: String? = null,
    val activeCollection: String? = null,
    
    val searchResults: List<Bookmark> = emptyList(),
    val searchPage: Int = 1,
    val searchTotalPages: Int = 1,
    val isSearchLoading: Boolean = false,
    val isSearchLoadingMore: Boolean = false,

    val collections: List<Collection> = emptyList(),
    val tags: List<Tag> = emptyList(),
    
    val isCardView: Boolean = true
)

class BookmarkViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(BookmarkUiState())
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadBookmarks(reset = true)
        loadTags()
        loadCollections()
    }

    fun loadBookmarks(reset: Boolean = true) {
        if (reset) {
            _uiState.update { it.copy(page = 1, isLoading = true, error = null) }
        } else {
            if (_uiState.value.page >= _uiState.value.totalPages || _uiState.value.isLoadingMore) return
            _uiState.update { it.copy(page = it.page + 1, isLoadingMore = true, error = null) }
        }

        viewModelScope.launch {
            try {
                val state = _uiState.value
                val response = ApiClient.api.fetchBookmarks(
                    filter = state.filter,
                    tag = state.activeTag,
                    collection = state.activeCollection,
                    sort = state.sort,
                    page = state.page
                )

                _uiState.update {
                    it.copy(
                        bookmarks = if (reset) response.bookmarks else it.bookmarks + response.bookmarks,
                        totalPages = response.totalPages,
                        isLoading = false,
                        isLoadingMore = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        error = e.localizedMessage
                    )
                }
            }
        }
    }

    fun setFilter(filter: String) {
        _uiState.update { it.copy(filter = filter, activeTag = null, activeCollection = null) }
        loadBookmarks(reset = true)
    }

    fun setSort(sort: String) {
        _uiState.update { it.copy(sort = sort) }
        loadBookmarks(reset = true)
    }

    fun setTagFilter(tag: String) {
        _uiState.update { it.copy(activeTag = tag, activeCollection = null, filter = "all") }
        loadBookmarks(reset = true)
    }

    fun setCollectionFilter(collectionId: String) {
        _uiState.update { it.copy(activeCollection = collectionId, activeTag = null, filter = "all") }
        loadBookmarks(reset = true)
    }

    fun clearFilters() {
        _uiState.update { it.copy(activeTag = null, activeCollection = null, filter = "all") }
        loadBookmarks(reset = true)
    }

    fun loadCollections() {
        viewModelScope.launch {
            try {
                val collections = ApiClient.api.fetchCollections()
                _uiState.update { it.copy(collections = collections) }
            } catch (e: Exception) {
                // handle error
            }
        }
    }

    fun loadTags() {
        viewModelScope.launch {
            try {
                val tags = ApiClient.api.fetchTags()
                _uiState.update { it.copy(tags = tags) }
            } catch (e: Exception) {
                // handle error
            }
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        if (query.isBlank()) {
            _uiState.update { it.copy(searchResults = emptyList(), searchPage = 1, searchTotalPages = 1) }
            return
        }

        searchJob = viewModelScope.launch {
            delay(400) // debounce
            searchBookmarks(reset = true)
        }
    }

    fun searchBookmarks(reset: Boolean = true) {
        if (reset) {
            _uiState.update { it.copy(searchPage = 1, isSearchLoading = true, error = null) }
        } else {
            if (_uiState.value.searchPage >= _uiState.value.searchTotalPages || _uiState.value.isSearchLoadingMore) return
            _uiState.update { it.copy(searchPage = it.searchPage + 1, isSearchLoadingMore = true, error = null) }
        }

        viewModelScope.launch {
            try {
                val state = _uiState.value
                val response = ApiClient.api.fetchBookmarks(
                    query = state.searchQuery,
                    page = state.searchPage
                )

                _uiState.update {
                    it.copy(
                        searchResults = if (reset) response.bookmarks else it.searchResults + response.bookmarks,
                        searchTotalPages = response.totalPages,
                        isSearchLoading = false,
                        isSearchLoadingMore = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSearchLoading = false,
                        isSearchLoadingMore = false,
                        error = e.localizedMessage
                    )
                }
            }
        }
    }

    fun deleteBookmark(id: String) {
        viewModelScope.launch {
            try {
                ApiClient.api.deleteBookmark(id)
                _uiState.update { state ->
                    state.copy(
                        bookmarks = state.bookmarks.filter { it.id != id },
                        searchResults = state.searchResults.filter { it.id != id }
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.localizedMessage) }
            }
        }
    }

    fun toggleArchive(bookmark: Bookmark) {
        viewModelScope.launch {
            try {
                val updated = ApiClient.api.updateBookmark(
                    id = bookmark.id,
                    request = BookmarkUpdateRequest(isArchived = !bookmark.isArchived)
                )
                updateBookmarkLocally(updated)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.localizedMessage) }
            }
        }
    }

    fun toggleReadLater(bookmark: Bookmark) {
        viewModelScope.launch {
            try {
                val updated = ApiClient.api.updateBookmark(
                    id = bookmark.id,
                    request = BookmarkUpdateRequest(isReadLater = !bookmark.isReadLater)
                )
                updateBookmarkLocally(updated)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.localizedMessage) }
            }
        }
    }

    private fun updateBookmarkLocally(updated: Bookmark) {
        _uiState.update { state ->
            val removeIfFiltered = (state.filter == "readlater" && !updated.isReadLater) ||
                                   (state.filter == "archived" && !updated.isArchived)

            val newBookmarks = if (removeIfFiltered) {
                state.bookmarks.filter { it.id != updated.id }
            } else {
                state.bookmarks.map { if (it.id == updated.id) updated else it }
            }

            state.copy(
                bookmarks = newBookmarks,
                searchResults = state.searchResults.map { if (it.id == updated.id) updated else it }
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
    
    fun toggleViewStyle() {
        _uiState.update { it.copy(isCardView = !it.isCardView) }
    }
}
