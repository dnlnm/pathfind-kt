package com.nxim.pathfind.data.model

data class Bookmark(
    val id: String,
    val url: String,
    val title: String?,
    val description: String?,
    val notes: String?,
    val favicon: String?,
    val thumbnail: String?,
    val isArchived: Boolean,
    val isReadLater: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val userId: String,
    val tags: List<BookmarkTag> = emptyList(),
    val collections: List<BookmarkCollection>? = null
) {
    val domain: String
        get() = try {
            val host = java.net.URL(url).host
            host.removePrefix("www.")
        } catch (e: Exception) {
            url
        }
}

data class BookmarkTag(
    val id: String,
    val name: String
)

data class BookmarkCollection(
    val id: String,
    val name: String,
    val color: String?
)

data class PaginatedBookmarkResponse(
    val bookmarks: List<Bookmark>,
    val total: Int,
    val page: Int,
    val totalPages: Int
)

data class BookmarkCreateRequest(
    val url: String,
    val title: String? = null,
    val notes: String? = null,
    val tags: List<String>? = null,
    val collections: List<String>? = null,
    val isReadLater: Boolean? = null
)

data class BookmarkUpdateRequest(
    val title: String? = null,
    val description: String? = null,
    val notes: String? = null,
    val tags: List<String>? = null,
    val collections: List<String>? = null,
    val isArchived: Boolean? = null,
    val isReadLater: Boolean? = null
)
