package com.example.smplplayer.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.smplplayer.R
import com.example.smplplayer.data.MusicItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CurrentPlayingDock(
    currentPlaying: MusicItem,
    onNextClick : () -> Unit,
    onPlayPauseClick : () -> Unit,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
            .then(modifier)
    ){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(currentPlaying.albumArt)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(end = 16.dp)
                .size(48.dp)
                .clip(RoundedCornerShape(6.dp))
        )

        Column(modifier = Modifier.weight(1f)){
            Text(text = currentPlaying.title, style = MaterialTheme.typography.titleSmall,modifier = Modifier.padding(horizontal = 8.dp).basicMarquee())
            Text(text = currentPlaying.artist, style = MaterialTheme.typography.bodySmall)
        }

        // Play/Pause button
        IconButton(
            onClick = {
                onPlayPauseClick()
            }
        ) {
            Icon(
                imageVector =
                if(isPlaying) ImageVector.vectorResource(R.drawable.ic_pause)
                else ImageVector.vectorResource(R.drawable.ic_play), contentDescription = null
            )
        }

        // Skip to next button
        IconButton(
            onClick = {
                onNextClick()
            }
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_next),
                contentDescription = null
            )
        }
    }
}
