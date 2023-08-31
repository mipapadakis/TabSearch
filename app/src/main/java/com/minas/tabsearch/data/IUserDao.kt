package com.minas.tabsearch.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface IUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Query("UPDATE user SET friendStatus=:friendStatus WHERE id = :userId")
    fun updateUser(userId: Int, friendStatus: FriendStatus)

    @Delete
    fun deleteUser(user: User)

    @Query("DELETE FROM user")
    fun deleteAllUsers()

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUser(id: Int): Flow<User?>

    @Query("SELECT * FROM user")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM user WHERE id LIKE :term OR name LIKE :term OR lastname LIKE :term OR username LIKE :term OR name || lastname LIKE :term")
    fun searchUsers(term: String): Flow<List<User>>

    @Query("SELECT * FROM user WHERE isFollowing = 1 AND (id LIKE :term OR name LIKE :term OR lastname LIKE :term OR username LIKE :term OR name || lastname LIKE :term)")
    fun searchFollowing(term: String): Flow<List<User>>

    @Query("SELECT * FROM user WHERE isFollower = 1 AND (id LIKE :term OR name LIKE :term OR lastname LIKE :term OR username LIKE :term OR name || lastname LIKE :term)")
    fun searchFollowers(term: String): Flow<List<User>>

    @Query("SELECT * FROM user WHERE isFollowing = 1")
    fun getFollowing(): Flow<List<User>>

    @Query("SELECT * FROM user WHERE isFollower = 1")
    fun getFollowers(): Flow<List<User>>
}