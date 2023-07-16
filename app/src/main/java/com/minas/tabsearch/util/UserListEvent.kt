package com.minas.tabsearch.util

import com.minas.tabsearch.data.User

data class UserListState(
    val eventName: UserListEvent = UserListEvent.None,
    val followingList: List<User> = emptyList(),
    val followersList: List<User> = emptyList()
)

enum class UserListEvent {
    None,
    LoadFollowing,
    LoadFollowers,
    SearchFollowing,
    SearchFollowers
}

interface IUserListEventHandler {
    fun setUserListEventNone()
    fun getFollowing()
    fun getFollowers()
    fun searchFollowing(term: String)
    fun searchFollowers(term: String)
}