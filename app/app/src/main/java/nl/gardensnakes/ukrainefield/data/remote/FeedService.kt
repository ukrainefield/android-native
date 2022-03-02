package nl.gardensnakes.ukrainefield.data.remote

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import nl.gardensnakes.ukrainefield.data.remote.dto.FeedResponse

interface FeedService {
    suspend fun getAllFeed(): FeedResponse?

    companion object{
        fun create(): FeedService{
            return FeedServiceImpl(
                client = HttpClient(Android) {
                    install(Logging){
                        level = LogLevel.ALL
                    }
                    install(JsonFeature){
                        serializer = KotlinxSerializer(json)
                    }
                }
            )
        }
        private val json = kotlinx.serialization.json.Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = false
        }
    }
}