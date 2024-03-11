package com.example.smplplayer.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import com.example.smplplayer.data.MusicItem
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier

@Composable
fun MusicList(
    musicList: List<MusicItem>,
    onMusicSelected: (MusicItem) -> Unit,
    currentMusicItem: MusicItem,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier
    ){
        items(
            musicList,
            key = { it.id  }
        ){
            MusicListItem(
                musicItem = it,
                isSelected = (it.id == currentMusicItem.id),
            ) {
                onMusicSelected(it)
            }
        }
    }
}