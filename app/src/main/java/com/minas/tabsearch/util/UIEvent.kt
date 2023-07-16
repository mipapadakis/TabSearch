package com.minas.tabsearch.util

sealed class UIEvent {
    data class Search(val term: String) : UIEvent()
    object CancelSearchMode : UIEvent()
    object PopBackStack : UIEvent()
}