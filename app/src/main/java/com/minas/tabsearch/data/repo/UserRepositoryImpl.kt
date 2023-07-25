package com.minas.tabsearch.data.repo

import com.minas.tabsearch.data.IUserDao
import com.minas.tabsearch.data.User
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(private val dao: IUserDao) : IUserRepository {
    override suspend fun insertUser(user: User) = dao.insertUser(user)
    override suspend fun updateUser(user: User) = dao.updateUser(user)
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