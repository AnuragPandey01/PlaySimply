package com.example.smplplayer.data

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.runtime.Stable
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata

@Stable
data class MusicItem(
    val id: Long,
    val title: String,
    val artist: String,
    val albumArt: Uri,
    val duration: Long,
    val audioUri: Uri
){
    fun toMediaItem(): MediaItem {
        return MediaItem.Builder()
            .setUri(audioUri)
            .setMediaId(id.toString())
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(title)
                    .setArtist(artist)
                    .setArtworkUri(albumArt)
                    .build()
            )
            .build()
    }
}

@SuppressLint("Range")
fun ContentResolver.getMusicList():List<MusicItem>{
    val list = mutableListOf<MusicItem>()

    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.ALBUM_ID
    )

    val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

    this.query(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        projection,
        selection,
        null,
        null
    )?.use {
        while (it.moveToNext()){
            val id = it.getLong(it.getColumnIndex(MediaStore.Audio.Media._ID))
            val uri = it.getString(it.getColumnIndex(MediaStore.Audio.Media.DATA))
            val name = it.getString(it.getColumnIndex(MediaStore.Audio.Media.TITLE))
            val artist = it.getString(it.getColumnIndex(MediaStore.Audio.Media.ARTIST))
            val albumId = it.getLong(it.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
            val duration = it.getLong(it.getColumnIndex(MediaStore.Audio.Media.DURATION))

            val albumArtUri = ContentUris.withAppendedId(
                Uri.parse("content://media/external/audio/albumart"),
                albumId
            )
            list.add(
                MusicItem(
                    id = id,
                    title = name,
                    artist = artist,
                    duration = duration,
                    albumArt = albumArtUri,
                    audioUri = Uri.parse(uri)
                )
            )
        }
    }
    return list
}

