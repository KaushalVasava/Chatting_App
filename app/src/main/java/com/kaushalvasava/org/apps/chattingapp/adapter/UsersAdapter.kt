package com.kaushalvasava.org.apps.chattingapp.adapter

import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kaushalvasava.org.apps.chattingapp.databinding.UserRowLayoutBinding
import com.kaushalvasava.org.apps.chattingapp.ui.users.UsersFragmentDirections
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User

class UsersAdapter :
    ListAdapter<User, UsersAdapter.MyViewHolder>(UserDiffCallback()) {

    class MyViewHolder(private val binding: UserRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val client = ChatClient.instance()
        fun bind(user: User) {
            binding.avatarImageView.setUserData(user)
            binding.usernameTextView.text = user.id
            binding.lastActiveTextView.text = convertDate(user.lastActive!!.time)
            binding.rootLayout.setOnClickListener {
                createNewChannel(user.id, this)
            }
        }

        private fun convertDate(milliseconds: Long): String {
            return DateFormat.format("dd/MM/yyyy hh:mm a", milliseconds).toString()
        }

        private fun createNewChannel(selectedUser: String, holder: MyViewHolder) {
            client.createChannel(
                channelType = "messaging",
                members = listOf(client.getCurrentUser()!!.id, selectedUser)
            ).enqueue { result ->
                if (result.isSuccess) {
                    navigateToChatFragment(holder, result.data().cid)
                } else {
                    Log.e("UsersAdapter", result.error().message.toString())
                }
            }
        }

        private fun navigateToChatFragment(holder: MyViewHolder, cid: String) {
            val action = UsersFragmentDirections.actionUsersFragmentToChatFragment(cid)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            UserRowLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentUser = currentList[position]
        holder.bind(currentUser)
    }

    class UserDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: User, newItem: User) =
            oldItem == newItem
    }
}











