package app.android.gallery.ui.components.mediaview.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.android.gallery.backend.model.Media
import app.android.gallery.ui.components.mediaview.MediaViewActions

@Composable
fun BottomBar(modifier: Modifier = Modifier, media: Media, onClickMoreOptions: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        MediaViewActions(media = media, onClickMoreOptions = onClickMoreOptions)
    }
}