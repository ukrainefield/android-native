package nl.gardensnakes.ukrainefield.data.remote

import nl.gardensnakes.ukrainefield.BuildConfig

object HttpRoutes {
    private const val ForceAccAPI: Boolean = true
    private const val BASE_URL_ACC = "https://ukrainefield-feed.mrproper.dev"
    private const val BASE_URL_DEV = "https://ukrainefield-feed.mrproper.dev"
    var feed = "${getBaseUrl()}/feed"

    private fun getBaseUrl() : String{
        if(BuildConfig.DEBUG && !ForceAccAPI){
            return BASE_URL_DEV
        }
        return BASE_URL_ACC
    }
}