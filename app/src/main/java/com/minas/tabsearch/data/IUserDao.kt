package com.minas.tabsearch.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface IUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUser(id: Int): User?

    @Query("SELECT * FROM user")
    fun getAllUsers(): Flow<List<User>>
}