package nl.gardensnakes.ukrainefield.helper

import android.content.Context
import nl.gardensnakes.ukrainefield.data.remote.dto.feed.FeedMessageResponse

class NewsFeedHelper {

    companion object {
        fun filterSourcesBasedOnPreferences(messages: List<FeedMessageResponse>, context: Context ): List<FeedMessageResponse> {
            var filteredMessages = messages;
            if(!PreferenceHelper.shouldShowRussianSources(context)){
                filteredMessages = removeMessagesWithCategory(filteredMessages, "russia")
            }
            if(!PreferenceHelper.shouldShowUkrainianSources(context)){
                filteredMessages = removeMessagesWithCategory(filteredMessages, "ukraine")
            }
            if(!PreferenceHelper.shouldShowTwitterSources(context)){
                filteredMessages = removeMessagesByType(filteredMessages, "twitter")
            }
            if(!PreferenceHelper.shouldShowTelegramSources(context)){
                filteredMessages = removeMessagesByType(filteredMessages, "telegram")
            }
            return filteredMessages
        }

        private fun removeMessagesWithCategory(messages: List<FeedMessageResponse>, category: String): List<FeedMessageResponse> {
            return messages.filter { message -> message.categories.indexOf(category) == -1 }
        }

        private fun removeMessagesByType(messages: List<FeedMessageResponse>, type: String): List<FeedMessageResponse> {
            return messages.filter { message -> message.type != type }
        }

    }
}