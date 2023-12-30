package app.android.gallery.ui.components.mediaview.photo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import app.android.gallery.R
import app.android.gallery.backend.model.Photo
import app.android.gallery.backend.viewmodel.PhotoViewModel
import app.android.gallery.ui.components.mediaview.components.BottomBar
import app.android.gallery.util.rememberZoomState
import app.android.gallery.util.zoomArea
import app.android.gallery.util.zoomImage
import coil.compose.AsyncImage

@Composable
fun PhotoView(
    media: Photo,
    photoViewModel: PhotoViewModel = viewModel(factory = PhotoViewModel.Factory),
    onClickMoreOptions: () -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures {
                    photoViewModel.showUi = !photoViewModel.showUi
                }
            },
        contentAlignment = Alignment.Center
    ) {
        val zoomState = rememberZoomState()

        val alpha by animateFloatAsState(
            targetValue = if (photoViewModel.showUi) 0.5f else 0f,
            label = "backgroundAlpha",
            animationSpec = tween(500)
        )
        AsyncImage(
            model = media.uri,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxSize()
                .blur(50.dp)
                .alpha(alpha)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zoomArea(zoomState),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = media.uri,
                contentDescription = stringResource(R.string.photo),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .zoomImage(zoomState),
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img)
            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black)
                        )
                    )
                    .align(Alignment.BottomCenter)
            ) {
                AnimatedVisibility(
                    visible = photoViewModel.showUi
                ) {
                    BottomBar(modifier = Modifier.padding(top = 30.dp), media, onClickMoreOptions)
                }
            }
        }
    }
}
