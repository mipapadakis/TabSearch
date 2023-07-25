package com.minas.tabsearch.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey var id: Int = 0,
    var userName: String = "",
    var name: String = "",
    var lastName: String = "",
    var isFollowing: Boolean = false,
    var isFollower: Boolean = false,
    var friendStatus: FriendStatus = FriendStatus.NotFriend
)

enum class FriendStatus {
    Friend,
    Added,
    Pending,
    NotFriend
}

fun friendStatusToString(status: FriendStatus) = when(status) {
    FriendStatus.Friend -> "Friends"
    FriendStatus.Added -> "Has sent you a friend request"
    FriendStatus.Pending -> "Pending"
    FriendStatus.NotFriend -> "Not Friends"
}