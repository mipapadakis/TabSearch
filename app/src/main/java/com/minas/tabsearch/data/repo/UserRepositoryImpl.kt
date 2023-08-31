package com.minas.tabsearch.data.repo

import com.minas.tabsearch.data.FriendStatus
import com.minas.tabsearch.data.IUserDao
import com.minas.tabsearch.data.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(private val dao: IUserDao) : IUserRepository {
    override suspend fun insertUser(user: User) = dao.insertUser(user)
    override fun updateUser(userId: Int, friendStatus: FriendStatus) = flow { emit(dao.updateUser(userId, friendStatus)) }
    override suspend fun deleteUser(user: User) = dao.deleteUser(user)
    override suspend fun deleteAllUsers() = dao.deleteAllUsers()
    override fun getUser(id: Int) = dao.getUser(id)
    override fun getAllUsers(): Flow<List<User>> = dao.getAllUsers()
    override fun searchUsers(term: String): Flow<List<User>> = dao.searchUsers("%${term.removeWhitespace()}%")
    override fun searchFollowing(term: String): Flow<List<User>> = dao.searchFollowing("%${term.removeWhitespace()}%")
    override fun searchFollowers(term: String): Flow<List<User>> = dao.searchFollowers("%${term.removeWhitespace()}%")
    override fun getFollowing(): Flow<List<User>> = dao.getFollowing()
    override fun getFollowers(): Flow<List<User>> = dao.getFollowers()

    private fun String.removeWhitespace() = this.replace(Regex("\\s+"), "")
}