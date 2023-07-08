package com.minas.tabsearch.data.repo

import com.minas.tabsearch.data.User
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    suspend fun insertUser(user: User)

    suspend fun deleteUser(user: User)

    suspend fun getUser(id: Int): User?

    fun getAllUsers(): Flow<List<User>>
}