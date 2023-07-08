package com.minas.tabsearch.data.repo

import com.minas.tabsearch.data.IUserDao
import com.minas.tabsearch.data.User
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val dao: IUserDao,
) : IUserRepository {
    override suspend fun insertUser(user: User) = dao.insertUser(user)
    override suspend fun deleteUser(user: User) = dao.deleteUser(user)
    override suspend fun getUser(id: Int): User? = dao.getUser(id)
    override fun getAllUsers(): Flow<List<User>> = dao.getAllUsers()
}