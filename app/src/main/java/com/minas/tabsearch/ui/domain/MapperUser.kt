package com.minas.tabsearch.ui.domain

import com.minas.tabsearch.data.User

fun User.toDomainUser() = DomainUser(
    id = id,
    userName = userName,
    name = name,
    lastName = lastName,
    isFollowing = isFollowing,
    isFollower = isFollower,
    friendStatus = friendStatus
)

fun DomainUser.toDataUser() = User(
    id = id,
    userName = userName,
    name = name,
    lastName = lastName,
    isFollowing = isFollowing,
    isFollower = isFollower,
    friendStatus = friendStatus
)

fun List<User>.toDomainUserList() = map { it.toDomainUser() }
