package com.nxim.pathfind.data.api

import com.nxim.pathfind.data.model.Bookmark
import com.nxim.pathfind.data.model.BookmarkCreateRequest
import com.nxim.pathfind.data.model.BookmarkUpdateRequest
import com.nxim.pathfind.data.model.Collection
import com.nxim.pathfind.data.model.CollectionCreateRequest
import com.nxim.pathfind.data.model.PaginatedBookmarkResponse
import com.nxim.pathfind.data.model.Tag
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PathFindApi {

    @GET("/api/bookmarks")
    suspend fun fetchBookmarks(
        @Query("filter") filter: String? = "all",
        @Query("q") query: String? = null,
        @Query("tag") tag: String? = null,
        @Query("collection") collection: String? = null,
        @Query("sort") sort: String? = "newest",
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 30
    ): PaginatedBookmarkResponse

    @POST("/api/bookmarks")
    suspend fun createBookmark(
        @Body request: BookmarkCreateRequest
    ): Bookmark

    @PUT("/api/bookmarks/{id}")
    suspend fun updateBookmark(
        @Path("id") id: String,
        @Body request: BookmarkUpdateRequest
    ): Bookmark

    @DELETE("/api/bookmarks/{id}")
    suspend fun deleteBookmark(
        @Path("id") id: String
    )

    @GET("/api/collections")
    suspend fun fetchCollections(): List<Collection>

    @POST("/api/collections")
    suspend fun createCollection(
        @Body request: CollectionCreateRequest
    ): Collection

    @GET("/api/tags")
    suspend fun fetchTags(): List<Tag>

}
