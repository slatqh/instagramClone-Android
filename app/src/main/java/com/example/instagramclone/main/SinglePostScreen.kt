package com.example.instagramclone.main



import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.instagramclone.IgViewModel
import com.example.instagramclone.data.PostData

@Composable
fun SinglePostScreen(navController: NavController, vm :IgViewModel, post: PostData? ){
    Text(text = "Single post screen ${post?.postDescription}")

}