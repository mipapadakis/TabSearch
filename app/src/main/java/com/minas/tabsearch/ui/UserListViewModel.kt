package com.minas.tabsearch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minas.tabsearch.data.FriendStatus
import com.minas.tabsearch.data.User
import com.minas.tabsearch.data.repo.IUserRepository
import com.minas.tabsearch.util.GenerateRandomUsers
import com.minas.tabsearch.util.UIEvent
import com.minas.tabsearch.util.UserListEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val repository: IUserRepository,
) : ViewModel() {
    val list = repository.getAllUsers()
    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: UserListEvent) {
        when (event) {
            is UserListEvent.OnUserClick -> onUserClick(event.user)
            is UserListEvent.OnChangeFriendStatus -> changeFriendStatus(event.user, event.status)
        }
    }

    fun onEvent(event: UIEvent) {
        when (event) {
            UIEvent.PopBackStack -> popBackStack()
            is UIEvent.Search -> search(event.term)
            is UIEvent.CancelSearchMode -> cancelSearch()
        }
    }

    fun generateUsers() {
        viewModelScope.launch {
            GenerateRandomUsers.asFlow(100).collect { user ->
//                val profilePic = GenerateRandomUsers.getRandomProfilePic(user)
//                println(
//                    "ID: ${user.id}, " +
//                        "Username: ${user.userName}, " +
//                        "Name: ${user.name} ${user.lastName}, " +
//                        "Friend Status: ${user.friendStatus}, " +
//                        "Profile Picture: $profilePic",
//                )
                repository.insertUser(user)
            }
        }
    }

    private fun onUserClick(user: User) { goToProfile(user) }
    private fun changeFriendStatus(user: User, newStatus: FriendStatus) {
        viewModelScope.launch {
            repository.insertUser(
                user.copy(friendStatus = newStatus),
            )
        }
    }
    private fun deleteUser(user: User) {
        viewModelScope.launch {
            repository.deleteUser(user)
        }
    }
    private fun popBackStack() {}
    private fun search(term: String) {
        //TODO search list
    }
    private fun cancelSearch() {}
    private fun goToProfile(user: User) {}

    private fun sendUiEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}