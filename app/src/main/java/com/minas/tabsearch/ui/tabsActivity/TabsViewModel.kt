package com.minas.tabsearch.ui.tabsActivity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minas.tabsearch.data.FriendStatus
import com.minas.tabsearch.data.User
import com.minas.tabsearch.data.repo.IUserRepository
import com.minas.tabsearch.util.GenerateRandomUsers
import com.minas.tabsearch.util.ViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class TabsViewModel(
    private val repository: IUserRepository
) : ViewModel(), ITabsEventHandler, IStatusActions {
    val tabsState = ViewState(TabsState())

    fun inFollowingTab() = tabsState.getStateCurrentValue().activeTab == Tabs.Following
    fun inFollowersTab() = tabsState.getStateCurrentValue().activeTab == Tabs.Followers

    fun generateUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            GenerateRandomUsers.asFlow(200).collect { user ->
                //val profilePic = GenerateRandomUsers.getRandomProfilePic(user)
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
        Log.d("TabSearch test: ViewModel", "getFollowing")
        searchFollowing(tabsState.getStateCurrentValue().searchTerm)
    }

    override fun getFollowers() {
        Log.d("TabSearch test: ViewModel", "getFollowers")
        searchFollowers(tabsState.getStateCurrentValue().searchTerm)
    }

    override fun searchFollowing(term: String) {
        Log.d("TabSearch test: ViewModel", "searchFollowing($term)")
        viewModelScope.launch {
            repository.searchFollowing(term).catch {
                showError(TabsError.ErrorSearchingFollowing, it.message ?: "")
            }.collect { list ->
                tabsState.updateState {
                    it.copy(
                        eventName = TabsEvent.LoadFollowing,
                        followingList = list
                    )
                }
            }
        }
    }

    override fun searchFollowers(term: String) {
        Log.d("TabSearch test: ViewModel", "searchFollowers($term)")
        viewModelScope.launch(Dispatchers.IO) {
            repository.searchFollowers(term).catch {
                showError(TabsError.ErrorSearchingFollowers, it.message ?: "")
            }.collect { list ->
                tabsState.updateState {
                    it.copy(
                        eventName = TabsEvent.LoadFollowers,
                        searchTerm = term,
                        followersList = list
                    )
                }
            }
        }
    }

    override fun onTabChange(activeTab: Tabs) {
        tabsState.updateState {
            it.copy(
                eventName = TabsEvent.OnTabChange,
                activeTab = activeTab
            )
        }
    }

    override fun onUserClick(user: User) {
        tabsState.updateState {
            it.copy(
                eventName = TabsEvent.OnUserClick,
                user = user
            )
        }
    }

    override fun search(term: String) {
        Log.d("TabSearch test: ViewModel", "search($term)")
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

    private fun onChangeFriendStatus(user: User, status: FriendStatus) {
        viewModelScope.launch(Dispatchers.IO) {
            //repository.updateUser(user.copy(friendStatus = status)) //WHY???????????????????? TODO
        }
    }

    override fun sendFriendRequest(user: User) {
        onChangeFriendStatus(user, FriendStatus.Pending)
    }

    override fun cancelFriendRequest(user: User) {
        onChangeFriendStatus(user, FriendStatus.NotFriend)
    }

    override fun acceptFriendRequest(user: User) {
        onChangeFriendStatus(user, FriendStatus.Friend)
    }

    override fun declineFriendRequest(user: User) {
        onChangeFriendStatus(user, FriendStatus.NotFriend)
    }

    override fun unFriend(user: User) {
        onChangeFriendStatus(user, FriendStatus.NotFriend)
    }
}