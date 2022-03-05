package nl.gardensnakes.ukrainefield.data.remote.dto.map

import kotlinx.serialization.Serializable

@Serializable
data class MapDataResponse(
    var imagePath: String,
    var displayUpdatedTime: String,
    var messageText: String,
    var messageLink: String,
    var epochTime: Int
)
