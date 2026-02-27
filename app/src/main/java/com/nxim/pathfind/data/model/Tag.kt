package com.nxim.pathfind.data.model

import com.google.gson.annotations.SerializedName

data class Tag(
    val id: String,
    val name: String,
    val createdAt: String?,
    @SerializedName("_count") val count: TagCount?
) {
    val bookmarkCount: Int
        get() = count?.bookmarks ?: 0
}

data class TagCount(
    val bookmarks: Int
)
