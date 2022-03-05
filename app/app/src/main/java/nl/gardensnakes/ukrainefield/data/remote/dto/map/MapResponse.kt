package nl.gardensnakes.ukrainefield.data.remote.dto.map

import kotlinx.serialization.Serializable

@Serializable
data class MapResponse(
    var mapData: List<MapDataResponse>
)

