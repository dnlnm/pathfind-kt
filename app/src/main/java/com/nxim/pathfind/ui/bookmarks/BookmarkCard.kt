package com.nxim.pathfind.ui.bookmarks

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nxim.pathfind.data.api.ApiClient
import com.nxim.pathfind.data.model.Bookmark

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun BookmarkCard(
    bookmark: Bookmark,
    isCompact: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        if (!isCompact) {
            Column(modifier = Modifier.fillMaxWidth()) {
                if (!bookmark.thumbnail.isNullOrEmpty()) {
                    val imageModel = remember(bookmark.thumbnail) { resolveThumbnailModel(bookmark.thumbnail, ApiClient.serverUrl) }
                    AsyncImage(
                        model = imageModel,
                        contentDescription = "Thumbnail",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    )
                }
                
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = bookmark.title ?: bookmark.domain,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (!bookmark.favicon.isNullOrEmpty()) {
                            val faviconModel = remember(bookmark.favicon) { resolveThumbnailModel(bookmark.favicon, ApiClient.serverUrl) }
                            AsyncImage(
                                model = faviconModel,
                                contentDescription = "Favicon",
                                modifier = Modifier.size(12.dp)
                            )
                        }
                        Text(
                            text = bookmark.domain,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (bookmark.isNsfw) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.errorContainer
                            ) {
                                Text(
                                    text = "NSFW",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    fontSize = 9.sp,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    if (bookmark.isReadLater || bookmark.tags.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            if (bookmark.isReadLater) {
                                Icon(Icons.Default.Bookmark, contentDescription = "Read Later", modifier = Modifier.size(14.dp))
                            }
                            bookmark.tags.take(3).forEach { tag ->
                                Text(
                                    text = "#${tag.name}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = bookmark.title ?: bookmark.domain,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (!bookmark.favicon.isNullOrEmpty()) {
                            val faviconModel = remember(bookmark.favicon) { resolveThumbnailModel(bookmark.favicon, ApiClient.serverUrl) }
                            AsyncImage(
                                model = faviconModel,
                                contentDescription = "Favicon",
                                modifier = Modifier.size(12.dp)
                            )
                        }
                        Text(
                            text = bookmark.domain,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (bookmark.isNsfw) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.errorContainer
                            ) {
                                Text(
                                    text = "NSFW",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    fontSize = 9.sp,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
                if (!bookmark.thumbnail.isNullOrEmpty()) {
                    val imageModel = remember(bookmark.thumbnail) { resolveThumbnailModel(bookmark.thumbnail, ApiClient.serverUrl) }
                    AsyncImage(
                        model = imageModel,
                        contentDescription = "Thumbnail",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .then(if (bookmark.isNsfw) Modifier.blur(12.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded) else Modifier)
                    )
                }
            }
        }
    }
}

fun resolveThumbnailModel(thumbnail: String, serverUrl: String): Any {
    if (thumbnail.startsWith("data:image", ignoreCase = true)) {
        try {
            val base64String = thumbnail.substringAfter("base64,")
            return android.util.Base64.decode(base64String, android.util.Base64.DEFAULT)
        } catch (e: Exception) {
            return thumbnail
        }
    }
    if (thumbnail.startsWith("http", ignoreCase = true)) return thumbnail
    
    val base = if (serverUrl.endsWith("/")) serverUrl.dropLast(1) else serverUrl
    val path = if (thumbnail.startsWith("/")) thumbnail else "/$thumbnail"
    return base + path
}
