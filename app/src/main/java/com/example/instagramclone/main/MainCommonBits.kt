package com.example.instagramclone.main

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.instagramclone.IgViewModel

@Composable
fun NotificationMessage(vm: IgViewModel){
    val notifState = vm.popupNotification.value
    val notifMessage = notifState?.getContentOrNull()
    if(notifMessage != null){
        Toast.makeText(LocalContext.current, notifMessage,Toast.LENGTH_LONG).show()
    }
}