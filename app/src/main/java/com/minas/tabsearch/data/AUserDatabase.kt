package com.minas.tabsearch.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [User::class],
    version = 1,
)
abstract class AUserDatabase : RoomDatabase() {
    abstract val dao: IUserDao
}