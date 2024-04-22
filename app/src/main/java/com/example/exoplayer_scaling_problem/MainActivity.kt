package com.example.exoplayer_scaling_problem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import okhttp3.OkHttpClient

class MainActivity : ComponentActivity() {
    private var player: Player? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val playerView = findViewById<PlayerView>(R.id.playerView)
        val newPlayer = createPlayer()
        playerView.player = newPlayer
        newPlayer.setMediaItem(MediaItem.fromUri("https://assets.mixkit.co/videos/preview/mixkit-yellow-and-white-flowers-in-a-tree-1181-large.mp4"))
        newPlayer.prepare()
        player = newPlayer
    }

    override fun onResume() {
        super.onResume()
        player?.play()
    }

    override fun onPause() {
        super.onPause()
        player?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.stop()
        player?.release()
    }

    @OptIn(UnstableApi::class)
    private fun createPlayer(): Player {
        val upstreamDataSourceFactory = OkHttpDataSource.Factory(OkHttpClient.Builder().build())
        val factory = DefaultDataSource.Factory(this, upstreamDataSourceFactory)
        val player = ExoPlayer.Builder(this)
            .setMediaSourceFactory(ProgressiveMediaSource.Factory(factory))
            .build()
        player.playWhenReady = true
        player.repeatMode = Player.REPEAT_MODE_ALL

        return player
    }
}
