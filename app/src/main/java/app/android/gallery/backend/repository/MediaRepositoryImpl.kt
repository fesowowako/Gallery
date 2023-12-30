package app.android.gallery.backend.repository

import android.content.ContentResolver
import android.content.Context
import android.database.MergeCursor
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import app.android.gallery.backend.model.Media
import app.android.gallery.backend.repository.util.getMedia
import app.android.gallery.backend.repository.util.mediaProjection
import app.android.gallery.util.observe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.Q)
class MediaRepositoryImpl(
    private val contentResolver: ContentResolver,
    private val context: Context
) : MediaRepository {

    private val contentUri =
        MediaStore.Images.Media.getContentUri(
            MediaStore.VOLUME_EXTERNAL
        )

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
                        mediaProjection,
                        null,
                        null,
                        sortOrder
                    ),
                    contentResolver.query(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        mediaProjection,
                        null,
                        null,
                        sortOrder
                    )
                )
            )
        query.use { cursor ->
            while (cursor.moveToNext()) {
                media.add(
                    cursor.getMedia(context)
                )
            }
        }

        media
    }
}
