package app.android.gallery.ui.components.mediaview.video

import android.text.format.DateUtils
import android.view.SoundEffectConstants
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.VolumeOff
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import app.android.gallery.R
import app.android.gallery.backend.model.Video
import app.android.gallery.backend.viewmodel.PhotoViewModel
import app.android.gallery.backend.viewmodel.PlayerViewModel
import app.android.gallery.ui.components.mediaview.components.BottomBar
import app.android.gallery.util.PlayerState
import app.android.gallery.util.playPause
import app.android.gallery.util.positionAndDurationState


@Composable
fun VideoView(
    video: Video,
    playWhenReady: Boolean = false,
    playerViewModel: PlayerViewModel = viewModel(),
    photoViewModel: PhotoViewModel = viewModel(factory = PhotoViewModel.Factory),
    onClickMoreOptions: () -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current
    val player = remember(context) { ExoPlayer.Builder(context).build() }
    player.playWhenReady = playWhenReady
    DisposableEffect(Unit) {
        view.keepScreenOn = true
        onDispose {
            view.keepScreenOn = false
        }
    }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                player.pause()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    DisposableEffect(Unit) {
        with(player) {
            val mediaItem = MediaItem.Builder().setUri(video.uri).build()
            setMediaItem(mediaItem)
            prepare()
            repeatMode = Player.REPEAT_MODE_ONE
            onDispose {
                player.release()
            }
        }
    }
    Box(
        Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    photoViewModel.showUi = !photoViewModel.showUi
                }
            },
        contentAlignment = Alignment.Center
    ) {
        AndroidView(factory = { context ->
            PlayerView(context).apply {
                this.player = player
                useController = false
            }
        }, modifier = Modifier.fillMaxSize())
        AnimatedVisibility(visible = photoViewModel.showUi, enter = fadeIn(), exit = fadeOut()) {
            with(player) {
                ElevatedCard(
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    ),
                    shape = CircleShape
                ) {
                    var playState by remember {
                        mutableStateOf(
                            if (isPlaying) {
                                PlayerState.Play
                            } else if (playbackState == 2 || playbackState == 1) {
                                PlayerState.Buffer
                            } else {
                                PlayerState.Pause
                            }
                        )
                    }

                    DisposableEffect(this) {
                        val listener = object : Player.Listener {
                            override fun onPlaybackStateChanged(playbackState: Int) {
                                if (playbackState == 2) {
                                    playState = PlayerState.Buffer
                                }
                            }

                            override fun onIsPlayingChanged(isPlaying: Boolean) {
                                playbackState
                                playState = if (isPlaying) {
                                    PlayerState.Play
                                } else if (playbackState == 2) {
                                    PlayerState.Buffer
                                } else {
                                    PlayerState.Pause
                                }
                            }
                        }
                        addListener(listener)
                        onDispose {
                            removeListener(listener)
                        }
                    }
                    IconButton(
                        onClick = {
                            view.playSoundEffect(SoundEffectConstants.CLICK)
                            player.playPause()
                        },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        when (playState) {
                            PlayerState.Buffer -> {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            }

                            PlayerState.Play -> {
                                Icon(
                                    Icons.Rounded.Pause,
                                    modifier = Modifier.size(24.dp),
                                    contentDescription = stringResource(R.string.pause)
                                )
                            }

                            PlayerState.Pause -> {
                                Icon(
                                    Icons.Rounded.PlayArrow,
                                    modifier = Modifier.size(24.dp),
                                    contentDescription = stringResource(R.string.play)
                                )
                            }
                        }
                    }
                }
            }
        }
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
                Column(Modifier.padding(top = 30.dp)) {
                    PlayerController(player, playerViewModel)
                    BottomBar(modifier = Modifier, video, onClickMoreOptions)
                }
            }
        }
    }
}

@Composable
fun PlayerController(
    player: Player,
    playerViewModel: PlayerViewModel,
    color: Color = Color.White
) {
    val view = LocalView.current
    with(player) {
        LaunchedEffect(playerViewModel.muted) {
            volume = if (playerViewModel.muted) 0f else 1f
        }
        Column(Modifier.padding(16.dp)) {
            val positionAndDuration by positionAndDurationState()
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(DateUtils.formatElapsedTime(positionAndDuration.first / 1000), color = color)
                var tempSliderPosition by remember { mutableStateOf<Float?>(null) }
                Slider(
                    modifier = Modifier.weight(1f),
                    value = tempSliderPosition ?: positionAndDuration.first.toFloat(),
                    onValueChange = { tempSliderPosition = it },
                    valueRange = 0f.rangeTo(
                        positionAndDuration.second?.toFloat() ?: Float.MAX_VALUE
                    ),
                    onValueChangeFinished = {
                        tempSliderPosition?.let {
                            seekTo(it.toLong())
                        }
                        tempSliderPosition = null
                    }, colors = SliderDefaults.colors(thumbColor = color, activeTrackColor = color)
                )
                Text(
                    positionAndDuration.second?.let { DateUtils.formatElapsedTime(it / 1000) }
                        ?: "", color = color
                )
                IconButton(onClick = {
                    view.playSoundEffect(SoundEffectConstants.CLICK)
                    playerViewModel.muted = !playerViewModel.muted
                }) {
                    if (!playerViewModel.muted) {
                        Icon(
                            Icons.Rounded.VolumeUp,
                            contentDescription = stringResource(R.string.mute_sound),
                            tint = color
                        )
                    } else {
                        Icon(
                            Icons.Rounded.VolumeOff,
                            contentDescription = stringResource(R.string.un_mute_sound),
                            tint = color
                        )
                    }
                }
            }
        }
    }
}