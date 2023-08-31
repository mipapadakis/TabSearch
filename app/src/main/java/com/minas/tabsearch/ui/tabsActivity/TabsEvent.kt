package com.minas.tabsearch.ui.tabsActivity

import com.minas.tabsearch.R
import com.minas.tabsearch.ui.domain.DomainUser

data class TabsState(
    val eventName: TabsEvent = TabsEvent.None,
    val activeTab: Tabs = Tabs.Following,
    val searchTerm: String = "",
    val userClicked: DomainUser? = null,
    val followingList: List<DomainUser> = emptyList(),
    val followersList: List<DomainUser> = emptyList(),
    val errorType: TabsError = TabsError.ErrorNone,
    val errorMessage: String = ""
)

enum class TabsEvent {
    None,
    LoadFollowing,
    LoadFollowers,
    Search,
    CancelSearchMode,
    UserClick,
    UpdateUser,
    TabChange,
    Error
}

enum class TabsError(val errorMessageId: Int) {
    ErrorNone(R.string.error_none),
    ErrorGeneric(R.string.error_generic),
    ErrorLoadingFollowing(R.string.error_loading_following),
    ErrorLoadingFollowers(R.string.error_loading_followers),
    ErrorSearchingFollowing(R.string.error_searching_following),
    ErrorSearchingFollowers(R.string.error_searching_followers)
}

enum class Tabs {
    Following,
    Followers
}

interface ITabsEventHandler {
    fun setEventNone()
    fun getFollowing()
    fun getFollowers()
    fun searchFollowing(term: String)
    fun searchFollowers(term: String)
    fun search(term: String)
    fun cancelSearchMode()
    fun onUserClick(user: DomainUser)
    fun onTabChange(activeTab: Tabs)
    fun showError(error: TabsError, errorMessage: String = "")
}
