package com.minas.tabsearch.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey var id: Int? = null,
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