package app.android.gallery.ui.components.album

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.android.gallery.backend.model.Album
import app.android.gallery.backend.viewmodel.AlbumDetailsViewModel
import app.android.gallery.navigation.Destination

@Composable
fun AlbumGrid(
    albums: List<Album>,
    onNavigate: (Destination) -> Unit,
    albumDetailsViewModel: AlbumDetailsViewModel = viewModel(factory = AlbumDetailsViewModel.Factory)
) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        columns = GridCells.Adaptive(100.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(albums) { album ->
            AlbumCard(album = album, onClickCard = {
                albumDetailsViewModel.getAlbumDetails(album)
                onNavigate(Destination.AlbumPage)
            })
        }
    }
}
