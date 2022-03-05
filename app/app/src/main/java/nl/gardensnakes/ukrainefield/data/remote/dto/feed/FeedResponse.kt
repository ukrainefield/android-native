package nl.gardensnakes.ukrainefield.data.remote.dto.feed

import kotlinx.serialization.Serializable

@Serializable
data class FeedResponse (
    val messages: List<FeedMessageResponse>
)
