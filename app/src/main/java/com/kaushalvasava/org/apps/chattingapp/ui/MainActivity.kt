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
            val pref = getSharedPreferences("USERS_DATA", MODE_PRIVATE)
            val userAlreadyExist = pref.getBoolean("logged", false)
            if (userAlreadyExist) {
                val user = if (currentUser != null) {
                    ChatUser(currentUser.name, currentUser.id)
                } else {
                    val userName = pref.getString("userName", "").toString()
                    val userId = pref.getString("userId", "").toString()
                    ChatUser(userName, userId)
                }
                val action = LoginFragmentDirections.actionLoginFragmentToChannelFragment(user)
                navController.navigate(action)
            }
        }

    }
}