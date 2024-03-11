package com.example.smplplayer

import android.content.ContentResolver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smplplayer.data.MusicItem
import com.example.smplplayer.data.getMusicList

class MainViewModel: ViewModel() {

    private val _controllerInitialised = MutableLiveData(false)
    val controllerInitialised = _controllerInitialised

    fun setControllerInitialised(value: Boolean){
        _controllerInitialised.value = value
    }

    private val _musicList = MutableLiveData<List<MusicItem>>(emptyList())
    val musicList: LiveData<List<MusicItem>> = _musicList

    fun fetchMusicList(contentResolver: ContentResolver){
        contentResolver.getMusicList().let {
            _musicList.value = it
        }
    }

    private val _currentMusicItem = MutableLiveData<MusicItem?>(null)
    val currentMusicItem: LiveData<MusicItem?> = _currentMusicItem

    fun setCurrentMusicItem(value: MusicItem){
        _currentMusicItem.value = value
    }

    fun setCurrentMusicItemByIndex(index: Int){
        _currentMusicItem.value = _musicList.value?.get(index)
    }
}