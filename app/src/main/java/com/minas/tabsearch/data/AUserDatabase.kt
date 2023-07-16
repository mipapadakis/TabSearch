package com.minas.tabsearch.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [User::class],
    version = 1,
)
abstract class AUserDatabase : RoomDatabase() {
    abstract val dao: IUserDao

    companion object {
        private const val DB_NAME = "user_db"
        fun newInstance(context: Context) =
            Room.databaseBuilder(context, AUserDatabase::class.java, DB_NAME).build()
    }
}