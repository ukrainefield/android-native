package nl.gardensnakes.ukrainefield.data.remote.dto.feed

import kotlinx.serialization.Serializable

@Serializable
data class TwitterDataResponse(
    val tweetID: String,
    val authorID: String,
    val authorUsername: String,
    val authorDisplayName: String,
    val profileImage: String
)
