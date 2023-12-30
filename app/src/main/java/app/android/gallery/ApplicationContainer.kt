package app.android.gallery

import android.content.ContentResolver
import android.content.Context
import android.os.Build
import app.android.gallery.backend.repository.AlbumRepository
import app.android.gallery.backend.repository.AlbumRepositoryFallbackImpl
import app.android.gallery.backend.repository.AlbumRepositoryImpl
import app.android.gallery.backend.repository.MediaRepository
import app.android.gallery.backend.repository.MediaRepositoryFallbackImpl
import app.android.gallery.backend.repository.MediaRepositoryImpl

interface ApplicationContainer {
    val mediaRepository: MediaRepository
    val albumRepository: AlbumRepository
}

class DefaultApplicationContainer(contentResolver: ContentResolver, context: Context) :
    ApplicationContainer {
    override val mediaRepository: MediaRepository by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaRepositoryImpl(contentResolver, context)
        } else {
            MediaRepositoryFallbackImpl(contentResolver, context)
        }
    }
    override val albumRepository: AlbumRepository by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            AlbumRepositoryImpl(contentResolver, context)
        } else {
            AlbumRepositoryFallbackImpl(contentResolver, context)
        }
    }
}
