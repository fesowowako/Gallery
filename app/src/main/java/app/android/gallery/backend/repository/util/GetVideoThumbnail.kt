package app.android.gallery.backend.repository.util

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.util.Size

/**
 * @param uri Video file uri
 */
fun getVideoThumbnail(uri: Uri, context: Context): Bitmap? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        runCatching {
            context.contentResolver.loadThumbnail(uri, Size(256, 256), null)
        }.getOrNull()
    } else {
        runCatching {
            MediaMetadataRetriever().apply {
                setDataSource(context, uri)
            }.frameAtTime
        }.getOrNull()
    }
}