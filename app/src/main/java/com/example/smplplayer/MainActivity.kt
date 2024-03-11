package com.example.smplplayer

import android.content.ComponentName
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.smplplayer.service.PlaybackService
import com.example.smplplayer.ui.components.RequestAudioPermission
import com.example.smplplayer.ui.screens.HomeScreen
import com.example.smplplayer.ui.theme.SmplPlayerTheme
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors

class MainActivity : ComponentActivity() {

    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private lateinit var controller: MediaController
    private val viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SmplPlayerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RequestAudioPermission(
                        onGranted = { Setup() },
                        onDismiss = { finish() }
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val sessionToken = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        controllerFuture = MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener(
            {
                controller = controllerFuture.get()

                controller.addListener(object : Player.Listener {

                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        super.onIsPlayingChanged(isPlaying)
                        viewModel.setIsPlaying(isPlaying)
                    }
                    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                        super.onMediaItemTransition(mediaItem, reason)
                        mediaItem?.let{
                            viewModel.setCurrentMusicItemByMediaId(it.mediaId)
                        }
                    }
                })

                viewModel.setControllerInitialised(true)
            },
            MoreExecutors.directExecutor()
        )
    }

    override fun onStop() {
        MediaController.releaseFuture(controllerFuture)
        super.onStop()
    }

    @Composable
    fun Setup() {

        LaunchedEffect(Unit){
            viewModel.fetchMusicList(contentResolver)
        }

        val isControllerInitialised by viewModel.controllerInitialised.observeAsState(false)
        val musicList by viewModel.musicList.observeAsState(emptyList())
        val isPlaying by viewModel.isPlaying.observeAsState(false)
        val currentMusic by viewModel.currentMusicItem.observeAsState(null)

        LaunchedEffect(isControllerInitialised){
            if(::controller.isInitialized) controller.setMediaItems(musicList.map { it.toMediaItem() })
        }

        HomeScreen(
            musicList = musicList,
            onNextClick = { controller.seekToNext() },
            onPlayPauseClick = { if(isPlaying) controller.pause() else controller.play() },
            onMusicSelected = { mi->
                controller.seekTo(musicList.indexOfFirst { mi.id == it.id }, 0)
                controller.play()
            },
            currentMusic = currentMusic ?: return,
            isPlaying = isPlaying
        )
    }
}