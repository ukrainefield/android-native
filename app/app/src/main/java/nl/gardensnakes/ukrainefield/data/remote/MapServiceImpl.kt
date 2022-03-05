package nl.gardensnakes.ukrainefield.data.remote

import io.ktor.client.*
import io.ktor.client.request.*
import nl.gardensnakes.ukrainefield.data.remote.dto.map.MapResponse

class MapServiceImpl(private val client: HttpClient): MapService {
    override suspend fun getAllMaps(): MapResponse? {
        return try {
            client.get {
                url("${HttpRoutes.map}/all")
            }
        } catch(e: Exception){
            null
        }
    }
}