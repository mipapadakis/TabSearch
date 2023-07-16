package com.minas.tabsearch.di

import com.minas.tabsearch.data.repo.IUserRepository
import com.minas.tabsearch.data.repo.UserRepositoryImpl
import com.minas.tabsearch.ui.UserListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val userListModule = module {
    viewModel { UserListViewModel(get()) }
    single<IUserRepository> { UserRepositoryImpl(get()) }
}