package com.minas.tabsearch

import android.app.Application
import com.minas.tabsearch.data.AUserDatabase
import com.minas.tabsearch.data.repo.IUserRepository
import com.minas.tabsearch.data.repo.UserRepositoryImpl
import com.minas.tabsearch.di.tabsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class TabSearchApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TabSearchApp)
            modules(appModule, tabsModule)
        }
    }
}

val appModule = module {
    single { AUserDatabase.newInstance(androidContext()) }
    single { get<AUserDatabase>().dao }
    single<IUserRepository> { UserRepositoryImpl(get()) }
}