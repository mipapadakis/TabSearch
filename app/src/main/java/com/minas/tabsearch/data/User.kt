package com.minas.tabsearch.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val id: Int? = null,
    val userName: String,
    val name: String = "",
    val lastName: String = "",
    val isFollowing: Boolean = false,
    val isFollower: Boolean = false,
    val friendStatus: FriendStatus = FriendStatus.NotFriend
)

enum class FriendStatus {
    Friend,
    Added,
    Pending,
    NotFriend
}