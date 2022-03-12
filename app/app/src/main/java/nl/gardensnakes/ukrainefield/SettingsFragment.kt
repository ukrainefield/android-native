package nl.gardensnakes.ukrainefield

import android.os.Bundle
import android.preference.*
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.firebase.analytics.FirebaseAnalytics
import nl.gardensnakes.ukrainefield.helper.BookmarkHelper

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var mFirebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireContext());
        mFirebaseAnalytics.setCurrentScreen(this.requireActivity(), this.javaClass.simpleName, this.javaClass.simpleName);
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
        findPreference<Preference>("reset_bookmarks")?.setOnPreferenceClickListener {
            context?.let { context -> BookmarkHelper().resetBookmarks(context) }
            true
        }
    }
}