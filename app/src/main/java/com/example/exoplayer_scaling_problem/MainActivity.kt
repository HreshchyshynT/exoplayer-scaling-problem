package com.example.exoplayer_scaling_problem

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.exoplayer_scaling_problem.ui.theme.ExoplayerscalingproblemTheme
import okhttp3.OkHttpClient

class MainActivity : ComponentActivity() {
    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val exoPlayer = rememberExoPlayer()
            LaunchedEffect(Unit) {
                exoPlayer.setMediaItem(MediaItem.fromUri("https://assets.mixkit.co/videos/preview/mixkit-yellow-and-white-flowers-in-a-tree-1181-large.mp4"))
                exoPlayer.play()
            }
            ExoplayerscalingproblemTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                        AndroidView(
                            modifier = Modifier.fillMaxSize(),
                            factory = {
                                PlayerView(it).apply {
                                    layoutParams = ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                    )
                                    this.useController = false
                                    setKeepContentOnPlayerReset(true)
                                    this.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                                }
                            },
                            update = {
                                it.player = exoPlayer
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun rememberExoPlayer(
    @Player.RepeatMode repeatMode: Int = Player.REPEAT_MODE_ALL,
    @C.VideoScalingMode scaleMode: Int = C.VIDEO_SCALING_MODE_DEFAULT,
): ExoPlayer {
    val context = LocalContext.current
    val exoPlayer = remember {
        val upstreamDataSourceFactory = OkHttpDataSource.Factory(OkHttpClient.Builder().build())
        val factory = DefaultDataSource.Factory(context, upstreamDataSourceFactory)
        val player = ExoPlayer.Builder(context)
            .setMediaSourceFactory(ProgressiveMediaSource.Factory(factory))
            .build()
        player
    }

    val lifecycleOwner by rememberUpdatedState(newValue = LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        val observer = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
            }

            override fun onPause(owner: LifecycleOwner) {
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            exoPlayer.stop()
            exoPlayer.release()
            lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(exoPlayer) {
        with(exoPlayer) {
            playWhenReady = false
            setRepeatMode(repeatMode)
            videoScalingMode = scaleMode
            prepare()
        }
    }
    return exoPlayer
}
