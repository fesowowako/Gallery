package app.android.gallery.ui.components.mediaview.infosheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Storage
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.android.gallery.R
import app.android.gallery.backend.model.Media
import app.android.gallery.ui.components.mediaview.MediaViewInfoActions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaInfoSheet(onDismissRequest: () -> Unit, media: Media) {
    val state = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(onDismissRequest, sheetState = state) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Row(
                Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                MediaViewInfoActions(media = media)
            }
            Divider(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
            Text(text = media.fullDate, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.details),
                style = MaterialTheme.typography.titleLarge
            )
            ListItem(
                headlineContent = { Text(text = stringResource(R.string.label)) },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Rounded.Image,
                        contentDescription = null
                    )
                },
                supportingContent = {
                    Text(text = media.label)
                })
            ListItem(
                headlineContent = { Text(text = stringResource(R.string.path)) },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Rounded.Storage,
                        contentDescription = null
                    )
                },
                supportingContent = {
                    Text(text = media.relativePath)
                })
        }
    }
}