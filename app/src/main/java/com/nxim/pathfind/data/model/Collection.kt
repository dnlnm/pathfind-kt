package com.nxim.pathfind.data.model

import com.google.gson.annotations.SerializedName

data class Collection(
    val id: String,
    val name: String,
    val description: String?,
    val icon: String?,
    val color: String?,
    @SerializedName("_count") val count: CollectionCount?
) {
    val bookmarkCount: Int
        get() = count?.bookmarks ?: 0
}

data class CollectionCount(
    val bookmarks: Int
)

data class CollectionCreateRequest(
    val name: String,
    val description: String? = null,
    val icon: String? = null,
    val color: String? = null
)
