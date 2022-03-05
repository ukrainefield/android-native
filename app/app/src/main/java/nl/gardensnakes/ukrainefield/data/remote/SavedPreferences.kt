package nl.gardensnakes.ukrainefield.data.remote

import android.content.Context
import android.preference.PreferenceManager

class SavedPreferences {
    companion object{
        fun useProxyServer(context: Context):Boolean{
            var preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getBoolean("useProxy", false)
        }
    }
}