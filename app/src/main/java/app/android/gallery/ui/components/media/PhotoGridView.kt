package app.android.gallery.ui.components.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import app.android.gallery.backend.viewmodel.PhotoViewModel

@Composable
fun PhotoGridView(
   onClickItem: (Int) -> Unit,
   photoViewModel: PhotoViewModel = rememberViewModel(factory = PhotoViewModel.Factory)
) {
   val photos by photoViewModel.photos.collectAsState()
   LazyColumn {
       items(photos, contentType = { it.hashCode() }) { photo ->
           GroupedPhotoGrid(photo, onClickItem)
       }
   }
}
