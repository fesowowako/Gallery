package app.android.gallery.backend.repository

import android.content.ContentResolver
import android.content.Context
import android.database.MergeCursor
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import app.android.gallery.backend.model.Album
import app.android.gallery.backend.model.AlbumWithMedia
import app.android.gallery.backend.model.Media
import app.android.gallery.backend.repository.util.albumProjection
import app.android.gallery.backend.repository.util.getAlbum
import app.android.gallery.backend.repository.util.getMedia
import app.android.gallery.backend.repository.util.mediaProjection
import app.android.gallery.util.observe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.Q)
class AlbumRepositoryImpl(
    private val contentResolver: ContentResolver,
    private val context: Context
) : AlbumRepository {
    private val contentUri =
        MediaStore.Images.Media.getContentUri(
            MediaStore.VOLUME_EXTERNAL
        )

    override fun getAllAlbumsStream(): Flow<List<Album>> =
        contentResolver.observe(contentUri).map { getAllAlbums() }

    private suspend fun getAllAlbums(): List<Album> = withContext(Dispatchers.IO) {
        val albumMap = mutableMapOf<Long, Album>()

        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} ASC"

        val query =
            MergeCursor(
                arrayOf(
                    contentResolver.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        albumProjection,
                        null,
                        null,
                        sortOrder
                    ),
                    contentResolver.query(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        albumProjection,
                        null,
                        null,
                        sortOrder
                    )
                )
            )
        query.use { cursor ->
            while (cursor.moveToNext()) {
                val album = cursor.getAlbum(context)
                val existingAlbum = albumMap[album.id]

                if (existingAlbum == null) {
                    albumMap[album.id as Long] = album
                } else {
                    existingAlbum.count++
                }
            }
        }

        albumMap.values.toList()
    }

    override fun getAlbumMediaStream(album: Album): Flow<AlbumWithMedia> =
        contentResolver.observe(contentUri).map {
            getAlbumWithMedia(album)
        }

    private suspend fun getAlbumWithMedia(album: Album): AlbumWithMedia =
        withContext(Dispatchers.IO) {
            val media = mutableListOf<Media>()

            val sortOrder = "${MediaStore.MediaColumns.DATE_TAKEN} ASC"
            val selection = "${MediaStore.MediaColumns.BUCKET_ID}=${album.id}"

            val query =
                MergeCursor(
                    arrayOf(
                        contentResolver.query(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            mediaProjection,
                            selection,
                            null,
                            sortOrder
                        ),
                        contentResolver.query(
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            mediaProjection,
                            selection,
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

            AlbumWithMedia(album, media)
        }
}
