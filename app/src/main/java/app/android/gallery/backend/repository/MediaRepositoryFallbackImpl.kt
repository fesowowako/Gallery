package app.android.gallery.backend.repository

import android.content.ContentResolver
import android.content.Context
import android.database.MergeCursor
import android.os.Build
import android.provider.MediaStore
import app.android.gallery.backend.model.Media
import app.android.gallery.backend.repository.util.getMediaFallback
import app.android.gallery.backend.repository.util.mediaProjectionFallback
import app.android.gallery.util.observe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MediaRepositoryFallbackImpl(
    private val contentResolver: ContentResolver,
    private val context: Context
) : MediaRepository {

    private val contentUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(
            MediaStore.VOLUME_EXTERNAL
        )
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    override fun getAllMediaStream(): Flow<List<Media>> = contentResolver.observe(contentUri).map {
        getAllMedia()
    }

    private suspend fun getAllMedia(): List<Media> = withContext(Dispatchers.IO) {
        val media = mutableListOf<Media>()

        val sortOrder = "${MediaStore.Images.Media.DATE_MODIFIED} DESC"

        val query =
            MergeCursor(
                arrayOf(
                    contentResolver.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        mediaProjectionFallback,
                        null,
                        null,
                        sortOrder
                    ),
                    contentResolver.query(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        mediaProjectionFallback,
                        null,
                        null,
                        sortOrder
                    )
                )
            )
        query.use { cursor ->
            while (cursor.moveToNext()) {
                media.add(
                    cursor.getMediaFallback(context)
                )
            }
        }

        media
    }
}
