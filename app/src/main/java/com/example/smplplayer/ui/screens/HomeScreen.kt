package com.example.smplplayer.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smplplayer.data.MusicItem
import com.example.smplplayer.ui.components.CurrentPlayingDock
import com.example.smplplayer.ui.components.MusicList

@Composable
fun HomeScreen(
    musicList: List<MusicItem>,
    onNextClick : () -> Unit,
    onPlayPauseClick : () -> Unit,
    onMusicSelected : (MusicItem) -> Unit,
    isPlaying: Boolean,
    currentMusic : MusicItem = musicList.first()
) {

    Box{
        MusicList(
            musicList = musicList,
            onMusicSelected = {
                onMusicSelected(it)
            },
            currentMusicItem = currentMusic,
            modifier = Modifier.padding(bottom = 100.dp)
        )
        CurrentPlayingDock(
            currentPlaying = currentMusic,
            onNextClick = { onNextClick() },
            onPlayPauseClick = { onPlayPauseClick() },
            isPlaying = isPlaying,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

}
