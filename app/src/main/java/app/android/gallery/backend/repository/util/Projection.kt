package app.android.gallery.backend.repository.util

import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.Q)
val albumProjection =
    arrayOf(
        MediaStore.MediaColumns.BUCKET_ID,
        MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
        MediaStore.MediaColumns.DISPLAY_NAME,
        MediaStore.MediaColumns.DATA,
        MediaStore.MediaColumns.RELATIVE_PATH,
        MediaStore.MediaColumns._ID,
        MediaStore.MediaColumns.MIME_TYPE,
        MediaStore.MediaColumns.DATE_MODIFIED,
        MediaStore.MediaColumns.DATE_TAKEN
    )

@RequiresApi(Build.VERSION_CODES.Q)
val mediaProjection =
    arrayOf(
        MediaStore.MediaColumns._ID,
        MediaStore.MediaColumns.DATA,
        MediaStore.MediaColumns.RELATIVE_PATH,
        MediaStore.MediaColumns.DISPLAY_NAME,
        MediaStore.MediaColumns.BUCKET_ID,
        MediaStore.MediaColumns.DATE_MODIFIED,
        MediaStore.MediaColumns.DATE_TAKEN,
        MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
        MediaStore.MediaColumns.DURATION,
        MediaStore.MediaColumns.MIME_TYPE,
        MediaStore.MediaColumns.ORIENTATION,
        MediaStore.MediaColumns.IS_FAVORITE,
        MediaStore.MediaColumns.IS_TRASHED
    )


val mediaProjectionFallback = arrayOf(
    MediaStore.MediaColumns._ID,
    MediaStore.MediaColumns.DATA,
    MediaStore.MediaColumns.DISPLAY_NAME,
    MediaStore.MediaColumns.DATE_MODIFIED,
    MediaStore.MediaColumns.MIME_TYPE
)