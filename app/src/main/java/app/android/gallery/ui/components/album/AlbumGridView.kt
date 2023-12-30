package app.android.gallery.ui.components.album

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import app.android.gallery.backend.viewmodel.AlbumViewModel
import app.android.gallery.navigation.Destination

@Composable
fun AlbumGridView(
    onNavigate: (Destination) -> Unit,
    albumViewModel: AlbumViewModel = viewModel(factory = AlbumViewModel.Factory)
) {
    val albums by albumViewModel.albums.collectAsState()
    AlbumGrid(albums, onNavigate)
}
