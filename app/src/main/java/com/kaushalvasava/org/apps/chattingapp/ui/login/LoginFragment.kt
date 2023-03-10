package com.kaushalvasava.org.apps.chattingapp.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kaushalvasava.org.apps.chattingapp.model.ChatUser
import com.google.android.material.textfield.TextInputLayout
import com.kaushalvasava.org.apps.chattingapp.R
import com.kaushalvasava.org.apps.chattingapp.databinding.FragmentLoginBinding
import com.kaushalvasava.org.apps.chattingapp.ui.MainActivity

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.button.setOnClickListener {
            authenticateTheUser()
        }

        return binding.root
    }

    private fun authenticateTheUser() {
        val firstName = binding.firstNameEditText.text.toString()
        val username = binding.usernameEditText.text.toString()
        if (validateInput(firstName, binding.firstNameInputLayout) &&
            validateInput(username, binding.usernameInputLayout)
        ) {
            val chatUser = ChatUser(firstName, username)
            requireContext().getSharedPreferences(MainActivity.USER_SHARED_PREF, Context.MODE_PRIVATE).edit().apply {
                putBoolean(MainActivity.LOGIN_STATUS_KEY, true)
                putString(MainActivity.USER_NAME_KEY, firstName)
                putString(MainActivity.USER_ID_KEY, username)
                apply()
            }
            val action = LoginFragmentDirections.actionLoginFragmentToChannelFragment(chatUser)
            findNavController().navigate(action)
        }
    }

    private fun validateInput(inputText: String, textInputLayout: TextInputLayout): Boolean {
        return if (inputText.length < 3) {
            textInputLayout.isErrorEnabled = true
            textInputLayout.error = getString(R.string.min_4_char_allow)
            false
        } else {
            textInputLayout.isErrorEnabled = false
            textInputLayout.error = null
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}