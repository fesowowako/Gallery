package app.android.gallery.ui.components.mediaview

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import app.android.gallery.backend.model.Media
import app.android.gallery.backend.model.Photo
import app.android.gallery.backend.model.Video
import app.android.gallery.ui.components.mediaview.infosheet.MediaInfoSheet
import app.android.gallery.ui.components.mediaview.photo.PhotoView
import app.android.gallery.ui.components.mediaview.video.VideoView

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaPageView(
    initialPage: Int,
    photos: List<Media>
) {
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        initialPageOffsetFraction = 0f
    ) {
        photos.size
    }
    var selectedMedia by remember { mutableStateOf<Media?>(null) }
    HorizontalPager(modifier = Modifier.fillMaxSize(), state = pagerState) {
        when (val item = photos[it]) {
            is Video -> {
                VideoView(
                    item,
                    playWhenReady = (it == pagerState.currentPage),
                    onClickMoreOptions = {
                        selectedMedia = item
                    })
            }

            is Photo -> {
                PhotoView(item, onClickMoreOptions = {
                    selectedMedia = item
                })
            }
        }
    }
    selectedMedia?.let {
        MediaInfoSheet(onDismissRequest = {
            selectedMedia = null
        }, it)
    }
}
