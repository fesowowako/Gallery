package app.android.gallery.backend.repository

import app.android.gallery.backend.model.Album
import app.android.gallery.backend.model.AlbumWithMedia
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {

    fun getAllAlbumsStream(): Flow<List<Album>>
    fun getAlbumMediaStream(album: Album): Flow<AlbumWithMedia>
}