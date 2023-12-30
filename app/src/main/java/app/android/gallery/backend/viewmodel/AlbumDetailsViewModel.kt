package app.android.gallery.backend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import app.android.gallery.GalleryApplication
import app.android.gallery.backend.model.Album
import app.android.gallery.backend.model.AlbumWithMedia
import app.android.gallery.backend.repository.AlbumRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class AlbumDetailsViewModel(private val albumRepository: AlbumRepository) : ViewModel() {
    lateinit var albumDetails: StateFlow<AlbumWithMedia>

    fun getAlbumDetails(album: Album) {
        albumDetails = albumRepository.getAlbumMediaStream(album).stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = AlbumWithMedia(album, listOf())
        )
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GalleryApplication
                AlbumDetailsViewModel(application.container.albumRepository)
            }
        }
    }
}