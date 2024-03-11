package com.example.smplplayer.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.smplplayer.data.MusicItem
import com.example.smplplayer.ui.components.MusicListItem

/*@Composable
fun HomeScreen(
    musicList: List<MusicItem>,
    mediaController: MediaController
) {

    LaunchedEffect(Unit){
        mediaController.addMediaItems(musicList.map { it.toMediaItem() })
    }

    LazyColumn{
        items(
            musicList,
            key = { it.id }
        ){mi->
            MusicListItem(mi){
                mediaController.seekTo(musicList.indexOfFirst { it.id == mi.id }, 0)
                mediaController.play()
            }
        }
    }
}*/

@Composable
fun MusicListDisplay(
    musicList: List<MusicItem>,
    modifier: Modifier = Modifier,
    onMusicItemClick : (MusicItem) -> Unit,
) {

    LazyColumn(
        modifier = modifier
    ){
        items(
            musicList,
            key = { it.id }
        ){mi->
            MusicListItem(mi){
                onMusicItemClick(mi)
            }
        }
    }
}