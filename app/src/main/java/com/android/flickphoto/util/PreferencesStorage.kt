package com.android.flickphoto.util

import android.content.Context
import com.android.flickphoto.R

private const val PREF_SEARCH_QUERY = "searchQuery"

object PreferencesStorage {
    fun getStoredQuery(context: Context): String {
        val prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(PREF_SEARCH_QUERY, "")!!
    }

    fun setStoredQuery(context: Context, query: String) {
        androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putString(PREF_SEARCH_QUERY, query)
            .apply()
    }

    fun getStoredScreenMode(context: Context): String {
        val prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
       return  prefs.getString(context.resources.getString(R.string.screen_mode_key),context.getString(R.string.system_mode))!!
    }
}
