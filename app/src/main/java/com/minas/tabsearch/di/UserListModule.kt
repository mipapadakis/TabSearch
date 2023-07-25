package com.minas.tabsearch.di

import com.minas.tabsearch.data.repo.IUserRepository
import com.minas.tabsearch.data.repo.UserRepositoryImpl
import com.minas.tabsearch.ui.tabsActivity.TabsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val tabsModule = module {
    viewModel { TabsViewModel(get()) }
    single<IUserRepository> { UserRepositoryImpl(get()) }
}