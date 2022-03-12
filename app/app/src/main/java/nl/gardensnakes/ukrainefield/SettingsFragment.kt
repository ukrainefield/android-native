package nl.gardensnakes.ukrainefield

import android.os.Bundle
import android.preference.*
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import nl.gardensnakes.ukrainefield.helper.BookmarkHelper

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
        findPreference<Preference>("reset_bookmarks")?.setOnPreferenceClickListener {
            context?.let { context -> BookmarkHelper().resetBookmarks(context) }
            true
        }
    }
}