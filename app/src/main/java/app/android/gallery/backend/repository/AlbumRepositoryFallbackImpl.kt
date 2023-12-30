package app.android.gallery.backend.repository

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import androidx.documentfile.provider.DocumentFile
import app.android.gallery.backend.model.Album
import app.android.gallery.backend.model.AlbumWithMedia
import app.android.gallery.backend.model.Photo
import app.android.gallery.backend.model.Video
import app.android.gallery.backend.repository.util.getVideoThumbnail
import app.android.gallery.util.observe
import com.anggrayudi.storage.file.DocumentFileCompat
import com.anggrayudi.storage.file.PublicDirectory
import com.anggrayudi.storage.file.mimeType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class AlbumRepositoryFallbackImpl(
    private val contentResolver: ContentResolver,
    private val context: Context
) : AlbumRepository {
    private val contentUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(
            MediaStore.VOLUME_EXTERNAL
        )
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    override fun getAllAlbumsStream(): Flow<List<Album>> =
        contentResolver.observe(contentUri).map { getAllAlbums() }

    private suspend fun getAllAlbums(): List<Album> = withContext(Dispatchers.IO) {
        val folder = DocumentFileCompat.fromPublicFolder(context, PublicDirectory.DCIM)
        val directories =
            folder?.listFiles()?.filter { it.isDirectory && it.name?.startsWith(".") == false }
        directories?.map {
            val files = it.listFiles().filter { f -> f.isFile }
            val file = files.first()
            val isPhoto = file?.mimeType?.contains("image") == true
            val thumbnail: Any? = file?.uri?.let {
                if (isPhoto) {
                    file.uri
                } else {
                    getVideoThumbnail(file.uri, context)
                }
            }
            Album(it, it.name.orEmpty(), thumbnail, "", 0, files.size)
        } ?: listOf()
    }

    override fun getAlbumMediaStream(album: Album): Flow<AlbumWithMedia> =
        contentResolver.observe(contentUri).map {
            getAlbumWithMedia(album)
        }

    private suspend fun getAlbumWithMedia(album: Album): AlbumWithMedia =
        withContext(Dispatchers.IO) {
            val directory = album.id as DocumentFile
            val media = directory.listFiles().filter { it.isFile }.map {
                val isPhoto = it.mimeType?.contains("image") == true
                if (isPhoto) {
                    Photo(
                        0,
                        it.name.orEmpty(),
                        it.uri,
                        it.uri.path.orEmpty(),
                        "",
                        0,
                        "",
                        0,
                        null,
                        "",
                        it.mimeType ?: "",
                        0,
                        0,
                        0
                    )
                } else {
                    val thumbnail: Bitmap? =
                        getVideoThumbnail(it.uri, context)

                    Video(
                        0,
                        it.name.orEmpty(),
                        it.uri,
                        it.uri.path.orEmpty(),
                        "",
                        0,
                        "",
                        0,
                        null,
                        "",
                        it.mimeType.orEmpty(),
                        0,
                        0,
                        0,
                        "",
                        thumbnail
                    )
                }
            }
            AlbumWithMedia(album, media)
        }
}
