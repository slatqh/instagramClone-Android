package com.example.instagramclone.main


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.instagramclone.IgViewModel
import com.example.instagramclone.R
import com.example.instagramclone.data.PostData

@Composable
fun SinglePostScreen(navController: NavController, vm: IgViewModel, post: PostData) {
    post.userId?.let {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth()
                .padding(8.dp)
        ) {
            Text(text = "Back", modifier = Modifier.clickable { navController.popBackStack() })
            CommonDivider()
            SinglePostDisplay(navController = navController, vm = vm, post = post)
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun SinglePostDisplay(navController: NavController, vm: IgViewModel, post: PostData) {
    val userData = vm.userData.value
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                shape = CircleShape, modifier = Modifier
                    .padding(8.dp)
                    .size(32.dp)
            ) {
                Image(
                    painter = rememberImagePainter(data = post.userImage), contentDescription = null
                )
            }
            Text(text = post.username ?: "")
            if (userData?.userId == post.userId) {
                //Current user's post. Don't show anything
            } else {
                Text(text = "Follow", color = Color.Blue, modifier = Modifier.clickable {
                    // Follow a user
                })
            }

        }
    }
    Box {
        val modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 150.dp)
        CommonImage(
            data = post.postImage,
            modifier = modifier,
            contentScale = ContentScale.FillWidth
        )
    }
    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.likes),
            contentDescription = null,
            modifier = Modifier.size(24.dp).padding(start = 0.dp),
            colorFilter = ColorFilter.tint(Color.Red)
        )
        Text(text = "${post.likes?.size ?: 0} likes", modifier = Modifier.padding(start = 4.dp))
    }
    Column (modifier = Modifier.padding(8.dp)) {
        Text(text = post.username ?: "", fontWeight = FontWeight.Bold)
        Text(text = post.postDescription ?: "", modifier = Modifier.padding(top = 18.dp))
    }
    Row(modifier = Modifier.padding(8.dp)){
        Text(text = "10 comments", color = Color.Gray, modifier = Modifier.padding(start = 8.dp))
    }
}