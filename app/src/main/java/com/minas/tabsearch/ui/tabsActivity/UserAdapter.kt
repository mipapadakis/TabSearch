package com.minas.tabsearch.ui.tabsActivity

import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.minas.tabsearch.R
import com.minas.tabsearch.data.FriendStatus
import com.minas.tabsearch.ui.domain.DomainUser
import com.minas.tabsearch.util.GenerateRandomUsers
import com.minas.tabsearch.util.hide
import com.minas.tabsearch.util.show

class UserAdapter(
    private val statusActions: IStatusActions,
    private val onItemClick: (user: DomainUser) -> Unit
) : ListAdapter<DomainUser, UserAdapter.UserViewHolder>(UserDiffCallback()) {

    class UserDiffCallback : DiffUtil.ItemCallback<DomainUser>() {
        override fun areItemsTheSame(oldItem: DomainUser, newItem: DomainUser): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DomainUser, newItem: DomainUser): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount() = currentList.size

    fun submitUserList(list: List<DomainUser>) {
        submitList(list)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val context = itemView.context
        val avatar: ImageView = itemView.findViewById(R.id.avatar)
        val name: TextView = itemView.findViewById(R.id.name)
        val username: TextView = itemView.findViewById(R.id.username)
        val statusButton: TextView = itemView.findViewById(R.id.status_button)
        val statusButtonDecline: TextView = itemView.findViewById(R.id.status_button_decline)

        fun bind(user: DomainUser) {
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
    fun sendFriendRequest(user: DomainUser)
    fun cancelFriendRequest(user: DomainUser)
    fun acceptFriendRequest(user: DomainUser)
    fun declineFriendRequest(user: DomainUser)
    fun unFriend(user: DomainUser)
}