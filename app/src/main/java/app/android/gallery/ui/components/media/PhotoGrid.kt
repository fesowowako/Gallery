package app.android.gallery.ui.components.media

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.android.gallery.backend.model.Media

@Composable
fun GroupedPhotoGrid(media: List<Media>, onClickItem: (Int) -> Unit) {
    val grouped = media.groupBy { it.fullDate }
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        columns = GridCells.Adaptive(100.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        grouped.entries.toList().forEach { (date, items) ->
            item(span = { GridItemSpan(this.maxLineSpan) }) {
                Text(
                    text = date,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 8.dp, 16.dp)
                )
            }
            items(items) { photo ->
                MediaCard(media = photo, onClickCard = { onClickItem(media.indexOf(photo)) })
            }
        }

    }
}

@Composable
fun PhotoGrid(media: List<Media>, onClickItem: (Int) -> Unit) {
    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        columns = GridCells.Adaptive(100.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(media) { index, photo ->
            MediaCard(media = photo, onClickCard = { onClickItem(index) })
        }

    }
}