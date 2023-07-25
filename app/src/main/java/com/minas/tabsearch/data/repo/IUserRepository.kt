package com.minas.tabsearch.data.repo

import com.minas.tabsearch.data.User
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    suspend fun insertUser(user: User)

    suspend fun updateUser(user: User)

    suspend fun deleteUser(user: User)

    suspend fun deleteAllUsers()

    fun getUser(id: Int): Flow<User?>

    fun getAllUsers(): Flow<List<User>>

    fun searchUsers(term: String): Flow<List<User>>

    fun searchFollowing(term: String): Flow<List<User>>

    fun searchFollowers(term: String): Flow<List<User>>

    fun getFollowing(): Flow<List<User>>

    fun getFollowers(): Flow<List<User>>
}