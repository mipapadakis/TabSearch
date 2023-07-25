package com.minas.tabsearch.ui.tabsActivity

import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.minas.tabsearch.R
import com.minas.tabsearch.data.FriendStatus
import com.minas.tabsearch.data.User
import com.minas.tabsearch.util.GenerateRandomUsers
import com.minas.tabsearch.util.hide
import com.minas.tabsearch.util.show

class UserAdapter(
    private val statusActions: IStatusActions,
    private val onItemClick: (user: User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private val users: MutableList<User> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount() = users.size

    fun submitList(list: List<User>) {
        users.clear()
        users.addAll(list)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val context = itemView.context
        val avatar: ImageView = itemView.findViewById(R.id.avatar)
        val name: TextView = itemView.findViewById(R.id.name)
        val username: TextView = itemView.findViewById(R.id.username)
        val statusButton: TextView = itemView.findViewById(R.id.status_button)
        val statusButtonDecline: TextView = itemView.findViewById(R.id.status_button_decline)

        fun bind(user: User) {
            avatar.setImageDrawable(
                BitmapDrawable(
                    context.resources,
                    GenerateRandomUsers.getRandomProfilePic(user)
                )
            )
            val nameLastname = "${user.name} ${user.lastName}"
            name.text = nameLastname
            username.text = user.userName
            itemView.setOnClickListener { onItemClick(user) }

            if (user.friendStatus == FriendStatus.Added) {
                statusButton.text = context.getString(R.string.status_button_accept)
                statusButtonDecline.show()
                statusButton.setOnClickListener {
                    statusButtonDecline.hide()
                    statusButton.text = context.getString(R.string.status_button_friends)
                    statusActions.acceptFriendRequest(user)
                }
                statusButtonDecline.setOnClickListener {
                    statusButtonDecline.hide()
                    statusButton.text = context.getString(R.string.status_button_not_friends)
                    statusActions.declineFriendRequest(user)
                }
            } else {
                statusButtonDecline.hide()
                when (user.friendStatus) {
                    FriendStatus.Friend -> {
                        statusButton.text = context.getString(R.string.status_button_friends)
                        statusButton.setOnClickListener {
                            statusButton.text = context.getString(R.string.status_button_not_friends)
                            statusActions.unFriend(user)
                        }
                    }
                    FriendStatus.NotFriend -> {
                        statusButton.text = context.getString(R.string.status_button_not_friends)
                        statusButton.setOnClickListener {
                            statusButton.text = context.getString(R.string.status_button_pending)
                            statusActions.sendFriendRequest(user)
                        }
                    }
                    FriendStatus.Pending -> {
                        statusButton.text = context.getString(R.string.status_button_pending)
                        statusButton.setOnClickListener {
                            statusButton.text = context.getString(R.string.status_button_not_friends)
                            statusActions.cancelFriendRequest(user)
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}

interface IStatusActions {
    fun sendFriendRequest(user: User)
    fun cancelFriendRequest(user: User)
    fun acceptFriendRequest(user: User)
    fun declineFriendRequest(user: User)
    fun unFriend(user: User)
}