package app.android.gallery.ui.components.albumview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.android.gallery.backend.viewmodel.AlbumDetailsViewModel
import app.android.gallery.ui.components.media.PhotoGrid

@Composable
fun AlbumView(
    onClickItem: (Int) -> Unit,
    albumDetailsViewModel: AlbumDetailsViewModel = viewModel(factory = AlbumDetailsViewModel.Factory)
) {
    val albumDetails by albumDetailsViewModel.albumDetails.collectAsState()
    Column(Modifier.fillMaxSize()) {
        Row(Modifier.padding(horizontal = 8.dp, vertical = 16.dp)) {
            Text(albumDetails.album.label, style = MaterialTheme.typography.headlineMedium)
        }
        PhotoGrid(media = albumDetails.media, onClickItem)
    }
}