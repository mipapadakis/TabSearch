package com.minas.tabsearch.ui.domain

import com.minas.tabsearch.data.FriendStatus

data class DomainUser(
    val id: Int = 0,
    val userName: String = "",
    val name: String = "",
    val lastName: String = "",
    val isFollowing: Boolean = false,
    val isFollower: Boolean = false,
    val friendStatus: FriendStatus = FriendStatus.NotFriend
)
