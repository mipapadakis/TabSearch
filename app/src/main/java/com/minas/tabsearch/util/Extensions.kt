package com.minas.tabsearch.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.show() {
    visibility = View.VISIBLE
}
fun View.hide() {
    visibility = View.GONE
}

fun Activity.hideKeyboard() {
    currentFocus?.let {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

private const val PREFS_NAME = "MyAppPrefs"
private const val KEY_FIRST_RUN = "firstRun"

fun Context.isFirstRun(): Boolean {
    val prefs = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val isFirstRun = prefs.getBoolean(KEY_FIRST_RUN, true)
    if (isFirstRun) {
        // Set firstRun flag to false, so next time it won't be considered the first run
        prefs.edit().putBoolean(KEY_FIRST_RUN, false).apply()
    }
    return isFirstRun
}
