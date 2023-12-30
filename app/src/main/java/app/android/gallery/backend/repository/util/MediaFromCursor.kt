package app.android.gallery.backend.repository.util

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import android.text.format.DateUtils
import androidx.annotation.RequiresApi
import app.android.gallery.backend.model.Media
import app.android.gallery.backend.model.Photo
import app.android.gallery.backend.model.Video
import app.android.gallery.util.getDate

@RequiresApi(Build.VERSION_CODES.Q)
fun Cursor.getMedia(context: Context): Media {
    val id: Long =
        getLong(getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
    val path: String =
        getString(getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))
    val relativePath: String =
        getString(getColumnIndexOrThrow(MediaStore.MediaColumns.RELATIVE_PATH))
    val title: String =
        getString(getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME))
    val albumID: Long =
        getLong(getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_ID))
    val albumLabel: String = try {
        getString(
            getColumnIndexOrThrow(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME)
        )
    } catch (_: Exception) {
        Build.MODEL
    }
    val takenTimestamp: Long? = try {
        getLong(getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_TAKEN))
    } catch (_: Exception) {
        null
    }
    val modifiedTimestamp: Long =
        getLong(getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED))
    val orientation: Int =
        getInt(getColumnIndexOrThrow(MediaStore.MediaColumns.ORIENTATION))
    val mimeType: String =
        getString(getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE))
    val isFavorite: Int =
        getInt(getColumnIndexOrThrow(MediaStore.MediaColumns.IS_FAVORITE))
    val isTrashed: Int =
        getInt(getColumnIndexOrThrow(MediaStore.MediaColumns.IS_TRASHED))
    val isPhoto = mimeType.contains("image")
    val contentUri = if (isPhoto) {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    } else {
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    }
    val uri = ContentUris.withAppendedId(contentUri, id)

    val formattedDate = modifiedTimestamp.getDate()

    return if (isPhoto) {
        Photo(
            id = id,
            label = title,
            uri = uri,
            path = path,
            relativePath = relativePath,
            albumID = albumID,
            albumLabel = albumLabel,
            timestamp = modifiedTimestamp,
            takenTimestamp = takenTimestamp,
            fullDate = formattedDate,
            favorite = isFavorite,
            trashed = isTrashed,
            orientation = orientation,
            mimeType = mimeType
        )
    } else {
        val thumbnail =
            getVideoThumbnail(uri, context)
        val duration: Long =
            getLong(getColumnIndexOrThrow(MediaStore.MediaColumns.DURATION))
        Video(
            id = id,
            label = title,
            uri = uri,
            path = path,
            relativePath = relativePath,
            albumID = albumID,
            albumLabel = albumLabel,
            timestamp = modifiedTimestamp,
            takenTimestamp = takenTimestamp,
            fullDate = formattedDate,
            duration = DateUtils.formatElapsedTime(duration / 1000),
            favorite = isFavorite,
            trashed = isTrashed,
            orientation = orientation,
            mimeType = mimeType,
            thumbnail = thumbnail
        )
    }
}

fun Cursor.getMediaFallback(context: Context): Media {
    val id: Long =
        getLong(getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
    val path: String =
        getString(getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))
    val title: String =
        getString(getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME))
    val modifiedTimestamp: Long =
        getLong(getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED))
    val mimeType: String =
        getString(getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE))

    val isPhoto = mimeType.contains("image")
    val contentUri = if (isPhoto) {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    } else {
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    }
    val uri = ContentUris.withAppendedId(contentUri, id)

    val formattedDate = modifiedTimestamp.getDate()

    return if (isPhoto) {
        Photo(
            id = id,
            label = title,
            uri = uri,
            path = path,
            relativePath = "",
            albumID = 0,
            albumLabel = Build.MODEL,
            timestamp = modifiedTimestamp,
            takenTimestamp = 0,
            fullDate = formattedDate,
            favorite = 0,
            trashed = 0,
            orientation = 0,
            mimeType = mimeType
        )
    } else {
        val thumbnail =
            getVideoThumbnail(uri, context)
        Video(
            id = id,
            label = title,
            uri = uri,
            path = path,
            relativePath = "",
            albumID = 0,
            albumLabel = Build.MODEL,
            timestamp = modifiedTimestamp,
            takenTimestamp = 0,
            fullDate = formattedDate,
            duration = "",
            favorite = 0,
            trashed = 0,
            orientation = 0,
            mimeType = mimeType,
            thumbnail = thumbnail
        )
    }
}