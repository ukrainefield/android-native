package nl.gardensnakes.ukrainefield.helper

import android.content.Context
import androidx.preference.PreferenceManager

class PreferenceHelper {
    companion object {
        fun shouldAutoPlayVideos(context: Context): Boolean {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return sharedPreferences.getBoolean("autoplayVideos", true)
        }
        fun shouldShowRussianSources(context: Context): Boolean {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return sharedPreferences.getBoolean("russianNewsSources", false)
        }
        fun shouldShowUkrainianSources(context: Context): Boolean {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return sharedPreferences.getBoolean("ukraineNewsSources", true)
        }
    }
}