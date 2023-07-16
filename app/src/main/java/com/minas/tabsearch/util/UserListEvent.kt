package com.minas.tabsearch.util

import com.minas.tabsearch.data.FriendStatus
import com.minas.tabsearch.data.User

sealed class UserListEvent {
    data class OnChangeFriendStatus(val user: User, val status: FriendStatus) : UserListEvent()
    data class OnUserClick(val user: User) : UserListEvent()
}