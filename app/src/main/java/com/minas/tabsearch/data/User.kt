package com.minas.tabsearch.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val id: Int? = null,
    val userName: String,
    val name: String = "",
    val lastName: String = "",
    val profilePic: String = ""
)