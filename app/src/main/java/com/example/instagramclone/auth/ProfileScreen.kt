package com.example.instagramclone.auth

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.instagramclone.DestinationScreen
import com.example.instagramclone.IgViewModel
import com.example.instagramclone.main.CommonDivider
import com.example.instagramclone.main.CommonImage
import com.example.instagramclone.main.CommonProgressSpiner
import com.example.instagramclone.main.navigateTo
import com.google.android.gms.common.internal.service.Common
import kotlin.contracts.contract


@Composable
fun ProfileScreen(navController: NavController, vm: IgViewModel) {
    val isLoading = vm.inProgress.value
    if (isLoading) {
        CommonProgressSpiner()
    } else {
        val userData = vm.userData.value
        var name by rememberSaveable { mutableStateOf(userData?.name ?: "") }
        var username by rememberSaveable { mutableStateOf(userData?.username ?: "") }
        var bio by rememberSaveable { mutableStateOf(userData?.bio ?: "") }
        ProfileContent(vm = vm,
            name = name,
            username = username,
            bio = bio,
            onNameChange = { name = it },
            onUsernameChange = { username = it },
            onBioChange = { bio = it },
            onSave = { vm.updateProfileData(name, username, bio) },
            onBack = { navigateTo(navController = navController, DestinationScreen.MyPosts) },
            onLogout = {
                vm.onLogout()
                navigateTo(navController, DestinationScreen.Login)
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    vm: IgViewModel,
    name: String,
    username: String,
    bio: String,
    onNameChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {

    val scrollState = rememberScrollState()
    val imageUrl = vm.userData.value?.imageUrl
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Back", modifier = Modifier.clickable { onBack.invoke() })
            Text(text = "Save", modifier = Modifier.clickable { onSave.invoke() })
        }
        CommonDivider()

        // User image upload
        ProfileImage(imageUrl = imageUrl, vm = vm)
        CommonDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Name", modifier = Modifier.width(100.dp))
            TextField(
                value = name,
                onValueChange = onNameChange,
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = Color.Black, background = Color.Transparent
                )
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Username", modifier = Modifier.width(100.dp))
            TextField(
                value = username,
                onValueChange = onUsernameChange,
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = Color.Black, background = Color.Transparent
                )
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(text = "Bio", modifier = Modifier.width(100.dp))
            TextField(
                value = bio,
                onValueChange = onBioChange,
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = Color.Black, background = Color.Transparent
                ),
                singleLine = false,
                modifier = Modifier.height(150.dp)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Logout",
                modifier = Modifier.clickable { onLogout.invoke() },
                color = Color.Red
            )
        }
    }
}

@Composable
fun ProfileImage(imageUrl: String?, vm: IgViewModel) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        uri: Uri? ->
        uri?.let { vm.uploadProfileImage(uri) }
    }
    Box(modifier = Modifier.height(IntrinsicSize.Min)) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable { launcher.launch("image/*")},
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = CircleShape, modifier = Modifier
                    .padding(8.dp)
                    .size(100.dp)
            ) {
                CommonImage(data = imageUrl)
            }
            Text(text = "Change profile picture")
        }
        val isLoading = vm.inProgress.value
        if (isLoading) {
            CommonProgressSpiner()
        }
    }
}
