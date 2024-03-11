package com.example.smplplayer

import android.content.ComponentName
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.smplplayer.service.PlaybackService
import com.example.smplplayer.ui.components.CurrentPlayingDock
import com.example.smplplayer.ui.components.RequestAudioPermission
import com.example.smplplayer.ui.screens.MusicListDisplay
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
                    // Fetch music list
                    LaunchedEffect(Unit){
                        viewModel.fetchMusicList(contentResolver)
                    }
                    val controllerInitialised by viewModel.controllerInitialised.observeAsState(false)
                    if(controllerInitialised){
                        RequestAudioPermission(
                            onGranted = {
                                HomeScreen()
                            },
                            onDismiss = {
                                finish()
                            }
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun HomeScreen() {
        LaunchedEffect(Unit){
            controller.addMediaItems(viewModel.musicList.value!!.map { it.toMediaItem() })
        }

        val musicList by viewModel.musicList.observeAsState(emptyList())
        val currentMusicItem by viewModel.currentMusicItem.observeAsState(null)

        Box {
            MusicListDisplay(
                musicList = musicList,
                modifier = Modifier.padding(bottom = if(currentMusicItem!= null) 100.dp else 0.dp)
            ){ mi ->
                viewModel.setCurrentMusicItem(mi)
                controller.seekTo(musicList.indexOfFirst { mi.id == it.id }, 0)
                controller.play()
            }

            if(currentMusicItem != null){
                CurrentPlayingDock(
                    currentPlaying = currentMusicItem!!,
                    onNextClick = {
                        controller.seekToNext()
                        viewModel.setCurrentMusicItemByIndex(controller.currentMediaItemIndex)
                    },
                    onPlayPauseClick = {
                        if(controller.isPlaying){
                            controller.pause()
                        }else{
                            controller.play()
                        }
                    },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
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
                viewModel.setControllerInitialised(true)
            },
            MoreExecutors.directExecutor()
        )
    }

    override fun onStop() {
        MediaController.releaseFuture(controllerFuture)
        super.onStop()
    }
}