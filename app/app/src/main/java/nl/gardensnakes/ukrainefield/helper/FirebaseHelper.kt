package nl.gardensnakes.ukrainefield.helper

import android.app.Activity
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class FirebaseHelper {
    companion object {
        fun updateCurrentScreen(firebase: FirebaseAnalytics, activity: Activity, activityName: String, className: String){
            firebase.setCurrentScreen(activity, activityName, className);
        }
        fun logShareEvent(firebase: FirebaseAnalytics, messageUrl: String){
            val params = Bundle()
            params.putString("share_message", messageUrl)
            firebase.logEvent("eventShareMessage", params)
        }
        fun logOpenInBrowserEvent(firebase: FirebaseAnalytics, messageUrl: String){
            val params = Bundle()
            params.putString("open_message_in_browser", messageUrl)
            firebase.logEvent("eventOpenMessageInBrowser", params)
        }
        fun logBookmarkEvent(firebase: FirebaseAnalytics, messageId: String){
            val params = Bundle()
            params.putString("message_id", messageId)
            firebase.logEvent("eventBookmarkMessage", params)
        }
        fun logUnbookmarkEvent(firebase: FirebaseAnalytics, messageUrl: String){
            val params = Bundle()
            params.putString("message_id", messageUrl)
            firebase.logEvent("eventUnbookmarkMessage", params)
        }
        fun logVisitOfficialLink(firebase: FirebaseAnalytics, linkUrl: String){
            val params = Bundle()
            params.putString("link_url", linkUrl)
            firebase.logEvent("eventVisitOfficialLink", params)
        }
        fun logResetBookmarks(firebase: FirebaseAnalytics){
            val params = Bundle()
            firebase.logEvent("eventResetBookmarks", params)
        }
    }
}