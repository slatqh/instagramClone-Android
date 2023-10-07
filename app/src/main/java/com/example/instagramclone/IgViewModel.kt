package com.example.instagramclone

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.instagramclone.data.Event
import com.example.instagramclone.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject

const val USERS = "users"
@HiltViewModel
class IgViewModel @Inject constructor(
    val auth: FirebaseAuth,
    val db: FirebaseFirestore,
    val storage: FirebaseStorage
) :ViewModel() {
    val signedIn = mutableStateOf(false)
    val inProgress = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)
    val popupNotification = mutableStateOf<Event<String>?>(null)

    init {
//        auth.signOut()
        val currentUser = auth.currentUser
        signedIn.value = currentUser != null
        currentUser?.uid?.let { uid ->
            getUserData(uid)
        }
    }
    fun onSignup(username:String, email:String, pass:String){

        if(email.isEmpty() or pass.isEmpty() or username.isEmpty()){
            handleException(customMessage =  "Email, username or pass can not be empty")
            return
        }
            inProgress.value = true

        db.collection(USERS).whereEqualTo("username",username).get()
            .addOnSuccessListener { documents ->
                if(documents.size() > 0){
                    handleException(customMessage = "Username already exist")
                    inProgress.value = false
                } else {
                    auth.createUserWithEmailAndPassword(email,pass)
                        .addOnCompleteListener{ task ->
                            if(task.isSuccessful){
                                signedIn.value = true
                                createOrUpdateProfile(username = username)
                            } else {
                                handleException(task.exception, "Signup failed")
                            }
                            inProgress.value = false
                        }

                }
            }
            .addOnFailureListener{

            }
    }
    fun onLogin(email: String, pass: String){
        if(email.isEmpty() or pass.isEmpty()){
            handleException(customMessage = "Email and pass can not be empty")
            return
        }
        inProgress.value = true
        auth.signInWithEmailAndPassword(email,pass)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    signedIn.value = true
                    inProgress.value = false
                    auth.currentUser?.uid?.let {uid ->
                        getUserData(uid)
                    }
                } else {
                    handleException(task.exception, "Login failed")
                    inProgress.value = false
                }
            }
            .addOnFailureListener{ exc ->
                handleException(exc, "Login failed")
                inProgress.value = false
            }
    }

    private fun createOrUpdateProfile(
        name: String? = null,
        username: String? = null,
        bio: String? = null,
        imageUrl:String? = null
    ) {
        val uid = auth.currentUser?.uid
        val userData = UserData(
            userId = uid,
            name = name?: userData.value?.name,
            username = username?: userData.value?.username,
            bio = bio?: userData.value?.bio,
            imageUrl = imageUrl?: userData.value?.imageUrl,
            following = userData.value?.following

        )
        uid?.let{
            inProgress.value = true
            db.collection(USERS).document(uid).get().addOnSuccessListener {
                if(it.exists()){
                    it.reference.update(userData.toMap()).addOnSuccessListener {
                        this.userData.value = userData
                        inProgress.value = false
                    }
                        .addOnFailureListener{
                            handleException(it,"Cannot update user")
                            inProgress.value = false
                        }
                } else {
                    db.collection(USERS).document(uid).set(userData)
                    getUserData(uid)
                    inProgress.value = false
                }
            }
                .addOnFailureListener{ exc ->
                    handleException(exc, "Cannot create user")
                    inProgress.value = false

                }
        }
    }
    private fun getUserData(uid: String){
        inProgress.value = true
        db.collection(USERS).document(uid).get()
            .addOnSuccessListener {
                val user = it.toObject<UserData>()
                userData.value = user
                inProgress.value= false
            }
            .addOnFailureListener{ exc ->
                handleException(exc, "Cannot retrieve user data")
            }
    }
    fun handleException(exception: Exception? = null, customMessage: String = " "){
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage ?: ""
        val message = if(customMessage.isEmpty()) errorMsg else "$customMessage : $errorMsg"
        popupNotification.value = Event(message)
    }
    fun updateProfileData(name:String, username: String,bio:String){
        createOrUpdateProfile(name, username, bio)
    }
}