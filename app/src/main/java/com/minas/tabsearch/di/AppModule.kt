package com.minas.tabsearch.di

import android.app.Application
import androidx.room.Room
import com.minas.tabsearch.data.AUserDatabase
import com.minas.tabsearch.data.repo.IUserRepository
import com.minas.tabsearch.data.repo.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUserDb(app: Application): AUserDatabase {
        return Room.databaseBuilder(
            app,
            AUserDatabase::class.java,
            "user_db",
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserRepo(db: AUserDatabase): IUserRepository {
        return UserRepositoryImpl(db.dao)
    }
}