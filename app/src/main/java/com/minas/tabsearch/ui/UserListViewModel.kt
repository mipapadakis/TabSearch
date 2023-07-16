package com.minas.tabsearch.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minas.tabsearch.data.FriendStatus
import com.minas.tabsearch.data.User
import com.minas.tabsearch.data.repo.IUserRepository
import com.minas.tabsearch.util.GenerateRandomUsers
import com.minas.tabsearch.util.IUiEventHandler
import com.minas.tabsearch.util.IUserListEventHandler
import com.minas.tabsearch.util.Tabs
import com.minas.tabsearch.util.UiError
import com.minas.tabsearch.util.UiEvent
import com.minas.tabsearch.util.UiState
import com.minas.tabsearch.util.UserListEvent
import com.minas.tabsearch.util.UserListState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserListViewModel(
    private val repository: IUserRepository
) : ViewModel(), IUiEventHandler, IUserListEventHandler {
    private val _listState = MutableStateFlow(UserListState())
    val listState = _listState.asStateFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

9    ////////////////////////////////////////////// DB //////////////////////////////////////////////

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

    private fun deleteUser(user: User) {
        viewModelScope.launch {
            repository.deleteUser(user)
        }
    }
    private fun insertUser(user: User) {
        viewModelScope.launch {
            repository.insertUser(user)
        }
    }

    override fun setUserListEventNone() {
        _listState.update { it.copy(eventName = UserListEvent.None) }
    }

    override fun getFollowing() {
        viewModelScope.launch {
            repository.getFollowing().catch {
                showError(UiError.ErrorLoadingFollowing, it.message ?: "")
            }.collect { list ->
                _listState.update {
                    it.copy(
                        eventName = UserListEvent.LoadFollowing,
                        followingList = list
                    )
                }
            }
        }
    }

    override fun getFollowers() {
        viewModelScope.launch {
            repository.getFollowers().catch {
                showError(UiError.ErrorLoadingFollowers, it.message ?: "")
            }.collect { list ->
                _listState.update {
                    it.copy(
                        eventName = UserListEvent.LoadFollowers,
                        followersList = list
                    )
                }
            }
        }
    }

    fun searchFollowing() = searchFollowing(uiState.value.searchTerm)
    override fun searchFollowing(term: String) {
        viewModelScope.launch {
            repository.searchFollowing(term).catch {
                showError(UiError.ErrorSearchingFollowing, it.message ?: "")
            }.collect { list ->
                _listState.update {
                    it.copy(
                        eventName = UserListEvent.SearchFollowing,
                        followingList = list
                    )
                }
            }
        }
    }

    fun searchFollowers() = searchFollowers(uiState.value.searchTerm)
    override fun searchFollowers(term: String) {
        viewModelScope.launch {
            repository.searchFollowers(term).catch {
                showError(UiError.ErrorSearchingFollowers, it.message ?: "")
            }.collect { list ->
                _listState.update {
                    it.copy(
                        eventName = UserListEvent.SearchFollowers,
                        followersList = list
                    )
                }
            }
        }
    }

    ////////////////////////////////////////////// UI //////////////////////////////////////////////

    override fun setUiEventNone() {
        _uiState.update { it.copy(eventName = UiEvent.None) }
    }

    fun inSearchMode() = uiState.value.searchTerm.isNotEmpty()

    override fun onTabChange(activeTab: Tabs) {
        _uiState.update {
            it.copy(
                eventName = UiEvent.OnTabChange,
                activeTab = activeTab
            )
        }
    }

    override fun onChangeFriendStatus(user: User, status: FriendStatus) {
        val updatedUser = user.copy(friendStatus = status)
        viewModelScope.launch {
            repository.updateUser(updatedUser)
            _uiState.update {
                it.copy(
                    eventName = UiEvent.OnChangeFriendStatus,
                    user = updatedUser
                )
            }
        }
    }

    fun goToProfile(user: User) { onUserClick(user) }
    override fun onUserClick(user: User) {
        _uiState.update {
            it.copy(
                eventName = UiEvent.OnUserClick,
                user = user
            )
        }
    }

    override fun search(term: String) {
        _uiState.update {
            it.copy(
                eventName = UiEvent.Search,
                searchTerm = term
            )
        }
    }

    override fun cancelSearchMode() {
        _uiState.update { it.copy(eventName = UiEvent.CancelSearchMode) }
    }

    override fun popBackStack() {
        _uiState.update { it.copy(eventName = UiEvent.PopBackStack) }
    }

    override fun showError(error: UiError, errorMessage: String) {
        _uiState.update {
            it.copy(
                eventName = UiEvent.Error,
                errorType = error,
                errorMessage = errorMessage
            )
        }
    }
}