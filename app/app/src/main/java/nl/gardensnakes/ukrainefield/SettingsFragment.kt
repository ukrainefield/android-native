package nl.gardensnakes.ukrainefield

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.*
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.firebase.analytics.FirebaseAnalytics
import nl.gardensnakes.ukrainefield.helper.BookmarkHelper
import nl.gardensnakes.ukrainefield.helper.Consts

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var mFirebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireContext());
        mFirebaseAnalytics.setCurrentScreen(this.requireActivity(), this.javaClass.simpleName, this.javaClass.simpleName);
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
        setupResetBookmarksClick()
        setupNewsSourcesClick()
        setupGithubReposClick()
        setupTwitterClick()
    }

    private fun setupResetBookmarksClick() {
        findPreference<Preference>("reset_bookmarks")?.setOnPreferenceClickListener {
            context?.let { context -> BookmarkHelper().resetBookmarks(context) }
            true
        }
    }

    private fun setupTwitterClick() {
        findPreference<Preference>("twitter_link")?.setOnPreferenceClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Consts.TWITTER_LINK))
            ContextCompat.startActivity(requireContext(), browserIntent, null)
            true
        }
    }

    private fun setupGithubReposClick() {
        findPreference<Preference>("github_link")?.setOnPreferenceClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Consts.GITHUB_LINK))
            ContextCompat.startActivity(requireContext(), browserIntent, null)
            true
        }
    }

    private fun setupNewsSourcesClick() {
        findPreference<Preference>("news_sources_link")?.setOnPreferenceClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Consts.NEWSSOURCES_LINK))
            ContextCompat.startActivity(requireContext(), browserIntent, null)
            true
        }
    }

}