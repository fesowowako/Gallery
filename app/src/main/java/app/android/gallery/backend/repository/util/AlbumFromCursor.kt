package app.android.gallery.backend.repository.util

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import app.android.gallery.backend.model.Album

@RequiresApi(Build.VERSION_CODES.Q)
fun Cursor.getAlbum(context: Context): Album {
    val id = getLong(getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_ID))
    val label: String? = try {
        getString(
            getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME)
        )
    } catch (e: Exception) {
        Build.MODEL
    }
    val mimeType: String =
        getString(getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE))
    val isPhoto = mimeType.contains("image")
    val contentUri = if (isPhoto) {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    } else {
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    }
    val mediaId: Long =
        getLong(getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
    val uri = ContentUris.withAppendedId(contentUri, mediaId)
    val thumbnail: Any? = if (isPhoto) {
        uri
    } else {
        getVideoThumbnail(uri, context)
    }
    val thumbnailRelativePath =
        getString(getColumnIndexOrThrow(MediaStore.MediaColumns.RELATIVE_PATH))
    val thumbnailDate =
        getLong(getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED))
    return Album(
        id = id,
        label = label ?: Build.MODEL,
        thumbnail = thumbnail,
        relativePath = thumbnailRelativePath,
        timestamp = thumbnailDate,
        count = 1
    )
}