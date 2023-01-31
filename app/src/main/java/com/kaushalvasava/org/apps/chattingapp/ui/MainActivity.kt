package com.kaushalvasava.org.apps.chattingapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.kaushalvasava.org.apps.chattingapp.R
import com.kaushalvasava.org.apps.chattingapp.model.ChatUser
import com.kaushalvasava.org.apps.chattingapp.ui.login.LoginFragmentDirections
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.name

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val client = ChatClient.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.navHostFragment)

        if (navController.currentDestination?.label.toString().contains("login")) {
            val currentUser = client.getCurrentUser()
            val pref = getSharedPreferences(USER_SHARED_PREF, MODE_PRIVATE)
            val userAlreadyExist = pref.getBoolean(LOGIN_STATUS_KEY, false)
            if (userAlreadyExist) {
                val user = if (currentUser != null) {
                    ChatUser(currentUser.name, currentUser.id)
                } else {
                    val userName = pref.getString(USER_NAME_KEY, "").toString()
                    val userId = pref.getString(USER_ID_KEY, "").toString()
                    ChatUser(userName, userId)
                }
                val action = LoginFragmentDirections.actionLoginFragmentToChannelFragment(user)
                navController.navigate(action)
            }
        }

    }

    companion object SharedPref {
        const val USER_SHARED_PREF = "users_data"
        const val USER_ID_KEY = "user_id"
        const val USER_NAME_KEY = "user_name"
        const val LOGIN_STATUS_KEY = "logged"
    }
}