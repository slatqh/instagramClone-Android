package com.example.instagramclone.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.instagramclone.IgViewModel

@Composable
fun MyPostScreen(navController: NavController, vm: IgViewModel){
    Column (modifier = Modifier.fillMaxSize()){
        Column (modifier = Modifier.weight(1f)){
            Text(text = "Post screen")
        }
        BottomNavigationMenu(
            selectedItem = BottomNavigationItem.POSTS,
            navController = navController
        )
    }
}