package com.android.flickphoto.ui.preferences

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.android.flickphoto.R

class FlickrPhotoSettings : PreferenceFragmentCompat() {
    private var callbacks:Callbacks? =null
    interface Callbacks{
        fun onSettingFragmentDisplayed()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }



    private var screenModePreference: ListPreference? = null
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)
        screenModePreference = findPreference(getString(R.string.screen_mode_key))

        screenModePreference?.summaryProvider =
            Preference.SummaryProvider<ListPreference> { preference ->
                "Selected Mode: ${preference.entry}"
            }
        screenModePreference?.setOnPreferenceChangeListener(object :
            Preference.OnPreferenceChangeListener {
            override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
                when (newValue) {
                    getString(R.string.night_mode) -> {
                        updateScreenMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    getString(R.string.light_mode) -> {
                        updateScreenMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    getString(R.string.system_mode) -> {
                        updateScreenMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    }
                }
                return true
            }

        })

    }
    fun updateScreenMode(mode:Int) {
        AppCompatDelegate.setDefaultNightMode(mode)
        requireActivity().recreate()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        callbacks?.onSettingFragmentDisplayed()
    }
}