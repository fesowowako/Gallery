package app.android.gallery.backend.repository

import app.android.gallery.backend.model.Media
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun getAllMediaStream(): Flow<List<Media>>
}
