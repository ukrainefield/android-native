package nl.gardensnakes.ukrainefield.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TelegramDataResponse(
    val messageId: String,
    val authorName: String,
)
