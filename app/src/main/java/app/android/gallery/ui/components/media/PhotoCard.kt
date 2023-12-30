package app.android.gallery.ui.components.media

import android.view.SoundEffectConstants
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.android.gallery.backend.model.Media
import app.android.gallery.backend.model.Video
import coil.compose.AsyncImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MediaCard(
    media: Media,
    onClickCard: () -> Unit,
    onLongPress: (() -> Unit)? = null
) {
    val view = LocalView.current
    val haptic = LocalHapticFeedback.current
    Box(
        Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    onClickCard()
                },
                onLongClick = {
                    onLongPress?.let {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        it.invoke()
                    }
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (media is Video) {
            AsyncImage(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp)),
                model = media.thumbnail,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Row(
                Modifier
                    .align(Alignment.TopEnd)
                    .clip(
                        RoundedCornerShape(50)
                    )
                    .background(MaterialTheme.colorScheme.secondaryContainer)

                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text(
                    media.duration,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Icon(
                    imageVector = Icons.Rounded.PlayCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        } else {
            AsyncImage(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp)),
                model = media.uri,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
    }
}
