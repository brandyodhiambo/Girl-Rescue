package com.adhanjadevelopers.girl_rescue.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.adhanjadevelopers.girl_rescue.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}