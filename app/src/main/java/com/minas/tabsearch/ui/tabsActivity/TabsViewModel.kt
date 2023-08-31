package com.minas.tabsearch.ui.tabsActivity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minas.tabsearch.data.FriendStatus
import com.minas.tabsearch.data.repo.IUserRepository
import com.minas.tabsearch.ui.domain.DomainUser
import com.minas.tabsearch.ui.domain.toDomainUserList
import com.minas.tabsearch.util.GenerateRandomUsers
import com.minas.tabsearch.util.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TabsViewModel(
    private val repository: IUserRepository
) : ViewModel(), ITabsEventHandler, IStatusActions {
    val tabsState = ViewState(TabsState())

    private fun inFollowingTab() = tabsState.getStateCurrentValue().activeTab == Tabs.Following

    fun generateUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            GenerateRandomUsers.asFlow(100).collect { user ->
                Log.d(
                    "TabSearch test",
                    "ID: ${user.id}, " +
                        "Username: ${user.userName}, " +
                        "Name: ${user.name} ${user.lastName}, " +
                        "Friend Status: ${user.friendStatus}, "
                )
                repository.insertUser(user)
            }
        }
    }

    fun refresh() { if(inFollowingTab()) getFollowing() else getFollowers() }

    override fun setEventNone() {
        tabsState.updateState { it.copy(eventName = TabsEvent.None) }
    }

    override fun getFollowing() {
        searchFollowing(tabsState.getStateCurrentValue().searchTerm)
    }

    override fun getFollowers() {
        searchFollowers(tabsState.getStateCurrentValue().searchTerm)
    }

    override fun searchFollowing(term: String) {
        viewModelScope.launch {
            repository.searchFollowing(term).catch {
                showError(TabsError.ErrorSearchingFollowing, it.message ?: "")
            }.collect { list ->
                tabsState.updateState {
                    it.copy(
                        eventName = TabsEvent.LoadFollowing,
                        followingList = list.toDomainUserList()
                    )
                }
            }
        }
    }

    override fun searchFollowers(term: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.searchFollowers(term).catch {
                showError(TabsError.ErrorSearchingFollowers, it.message ?: "")
            }.collect { list ->
                tabsState.updateState {
                    it.copy(
                        eventName = TabsEvent.LoadFollowers,
                        followersList = list.toDomainUserList()
                    )
                }
            }
        }
    }

    override fun onTabChange(activeTab: Tabs) {
        tabsState.updateState {
            it.copy(
                eventName = TabsEvent.TabChange,
                activeTab = activeTab
            )
        }
    }

    override fun onUserClick(user: DomainUser) {
        tabsState.updateState {
            it.copy(
                eventName = TabsEvent.UserClick,
                userClicked = user
            )
        }
    }

    override fun search(term: String) {
        tabsState.updateState {
            it.copy(
                eventName = TabsEvent.Search,
                searchTerm = term
            )
        }
    }

    override fun cancelSearchMode() {
        tabsState.updateState {
            it.copy(
                eventName = TabsEvent.CancelSearchMode,
                searchTerm = ""
            )
        }
    }

    override fun showError(error: TabsError, errorMessage: String) {
        tabsState.updateState {
            it.copy(
                eventName = TabsEvent.Error,
                errorType = error,
                errorMessage = errorMessage
            )
        }
    }

    private fun onChangeFriendStatus(user: DomainUser, status: FriendStatus) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user.id, status).catch {
                showError(TabsError.ErrorGeneric, it.message ?: "")
            }.collectLatest {
                tabsState.updateState { it.copy(eventName = TabsEvent.UpdateUser) }
            }
        }
    }

    override fun sendFriendRequest(user: DomainUser) {
        onChangeFriendStatus(user, FriendStatus.Pending)
    }

    override fun cancelFriendRequest(user: DomainUser) {
        onChangeFriendStatus(user, FriendStatus.NotFriend)
    }

    override fun acceptFriendRequest(user: DomainUser) {
        onChangeFriendStatus(user, FriendStatus.Friend)
    }

    override fun declineFriendRequest(user: DomainUser) {
        onChangeFriendStatus(user, FriendStatus.NotFriend)
    }

    override fun unFriend(user: DomainUser) {
        onChangeFriendStatus(user, FriendStatus.NotFriend)
    }
}