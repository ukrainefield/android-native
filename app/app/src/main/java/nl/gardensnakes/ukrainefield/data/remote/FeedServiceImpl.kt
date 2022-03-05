package nl.gardensnakes.ukrainefield.data.remote

import io.ktor.client.*
import io.ktor.client.request.*
import nl.gardensnakes.ukrainefield.data.remote.dto.feed.FeedResponse

class FeedServiceImpl (private val client: HttpClient) : FeedService {
    override suspend fun getAllFeed(): FeedResponse? {
        return try {
            client.get {
                url("${HttpRoutes.feed}/all")
            }
        } catch(e: Exception){
            null
        }
    }
}