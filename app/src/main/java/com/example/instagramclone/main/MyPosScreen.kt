package com.example.instagramclone.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.instagramclone.IgViewModel
import org.checkerframework.common.subtyping.qual.Bottom

@Composable
fun MyPostScreen(navController: NavController, vm: IgViewModel){

    val userData = vm.userData.value
    val isLoading = vm.inProgress.value

    Column {
        Column(modifier = Modifier.weight(1f)){
            Row {
                ProfileImage(userData?.imageUrl){

                }
                Text(
                    text ="15\nposts",
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center
                    )
                Text(
                    text ="55\nfollowers",
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center
                )
                Text(
                    text ="92\nfollowing",
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center
                )
            }
            Column (modifier = Modifier.padding(8.dp)){
                val usernameDisplay = if(userData?.username === null)"" else "@${userData.username}"
                Text(text= userData?.name ?: "", fontWeight = FontWeight.Bold)
                Text(text = usernameDisplay)
                Text(text = userData?.bio ?: "")
            }
            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    disabledElevation = 0.dp
                ),
                shape = RoundedCornerShape(10)
                ){
                Text(text = "Edit Profile", color = Color.Black)
            }
            Column (modifier = Modifier.weight(1f)){
                Text(text = "Post list")
            }
        }
        BottomNavigationMenu(selectedItem = BottomNavigationItem.POSTS, navController = navController)
    }



//    Column (modifier = Modifier.fillMaxSize()){
//        Column (modifier = Modifier.weight(1f)){
//            Text(text = "Post screen")
//        }
//        BottomNavigationMenu(
//            selectedItem = BottomNavigationItem.POSTS,
//            navController = navController
//        )
//    }
}

@Composable
fun ProfileImage(imageUrl:String?, onClick: () -> Unit){}