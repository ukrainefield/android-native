package nl.gardensnakes.ukrainefield.data.remote.dto.feed

import kotlinx.serialization.Serializable

@Serializable
data class FeedMessageResponse(
    val created_at: String,
    val text: String,
    val messageURL: String?,
    val images: List<String>,
    val videos: List<String>,
    val author: String,
    val epochTime: Int,
    val categories: List<String>,
    val type: String,
    val twitterData: TwitterDataResponse? = null,
    val telegramData: TelegramDataResponse? = null,
)
