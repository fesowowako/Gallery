package app.android.gallery.ui.components.mediaview

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.android.gallery.R
import app.android.gallery.backend.model.Media
import app.android.gallery.backend.model.Video
import app.android.gallery.util.launchEditIntent
import app.android.gallery.util.launchOpenWithIntent
import app.android.gallery.util.launchUseAsIntent
import app.android.gallery.util.shareMedia

@Composable
fun MediaViewInfoActions(
    media: Media,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        val context = LocalContext.current
        BottomBarColumn(
            currentMedia = media,
            imageVector = Icons.Rounded.OpenInNew,
            title = if (media is Video) stringResource(R.string.open_with) else stringResource(R.string.use_as),
            color = color
        ) {
            if (media is Video) {
                context.launchOpenWithIntent(it)
            } else {
                context.launchUseAsIntent(it)
            }
        }
    }
}

@Composable
fun MediaViewActions(
    media: Media,
    color: Color = Color.White,
    onClickMoreOptions: () -> Unit
) {
    val context = LocalContext.current
    BottomBarColumn(
        currentMedia = media,
        imageVector = Icons.Rounded.Share,
        title = stringResource(R.string.share),
        color = color
    ) {
        context.shareMedia(media = it)
    }
    BottomBarColumn(
        currentMedia = media,
        imageVector = Icons.Rounded.Edit,
        title = stringResource(R.string.edit),
        color = color
    ) {
        context.launchEditIntent(it)
    }
    BottomBarColumn(
        currentMedia = media,
        imageVector = Icons.Rounded.Info,
        title = stringResource(R.string.more),
        color = color
    ) {
        onClickMoreOptions.invoke()
    }
}

@Composable
fun BottomBarColumn(
    currentMedia: Media?,
    imageVector: ImageVector,
    title: String,
    color: Color,
    onItemClick: (Media) -> Unit,
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .defaultMinSize(
                minWidth = 90.dp,
                minHeight = 80.dp
            )
            .clickable {
                currentMedia?.let {
                    onItemClick.invoke(it)
                }
            }
            .padding(top = 12.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = imageVector,
            colorFilter = ColorFilter.tint(color),
            contentDescription = title,
            modifier = Modifier
                .height(32.dp)
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = title,
            modifier = Modifier,
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.bodyMedium,
            color = color,
            textAlign = TextAlign.Center
        )
    }
}
