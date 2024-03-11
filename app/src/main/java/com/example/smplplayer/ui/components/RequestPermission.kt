package com.example.smplplayer.ui.components

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestAudioPermission(  onGranted: @Composable () -> Unit,onDismiss:()->Unit) {

    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
        onGranted()
        return
    }

    val audioPermissionState = rememberPermissionState(Manifest.permission.READ_MEDIA_AUDIO)

    when(audioPermissionState.status){
        is PermissionStatus.Granted ->{
            onGranted()
        }
        is PermissionStatus.Denied -> {
            val msg = "The app requires audio access to load musics"
            val text= "Grant"
            AlertDialog(
                icon = {
                    Icon(Icons.Filled.Warning, contentDescription = null, modifier = Modifier.size(24.dp))
                },
                title = {
                    Text(text = "Permission Required")
                },
                text = {
                    Text(text = msg)
                },
                onDismissRequest = {
                    onDismiss()
                },
                confirmButton = {
                    TextButton(onClick = { audioPermissionState.launchPermissionRequest() }) {
                        Text(text = text)
                    }
                }
            )
        }
    }
}