package com.minas.tabsearch.util

import com.minas.tabsearch.R
import com.minas.tabsearch.data.FriendStatus
import com.minas.tabsearch.data.User

data class UiState(
    val eventName: UiEvent = UiEvent.None,
    val activeTab: Tabs = Tabs.Following,
    val searchTerm: String = "",
    val user: User? = null,
    val errorType: UiError = UiError.ErrorGeneric,
    val errorMessage: String = ""
)

enum class UiEvent {
    None,
    Error,
    OnTabChange,
    OnChangeFriendStatus,
    OnUserClick,
    Search,
    CancelSearchMode,
    PopBackStack
}

enum class UiError(val errorMessageId: Int) {
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

interface IUiEventHandler {
    fun setUiEventNone()
    fun onTabChange(activeTab: Tabs)
    fun onChangeFriendStatus(user: User, status: FriendStatus)
    fun onUserClick(user: User)
    fun search(term: String)
    fun cancelSearchMode()
    fun popBackStack()
    fun showError(error: UiError, errorMessage: String = "")
}
