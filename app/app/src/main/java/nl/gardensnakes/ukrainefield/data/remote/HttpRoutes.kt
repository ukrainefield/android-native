package nl.gardensnakes.ukrainefield.data.remote

import nl.gardensnakes.ukrainefield.BuildConfig

object HttpRoutes {
    private const val ForceAccAPI: Boolean = true
    private const val BASE_URL_ACC = "https://ukrainefield-feed.mrproper.dev"
    private const val BASE_URL_DEV = "https://ukrainefield-feed.mrproper.dev"
    const val MEDIA_PROXY = "https://ukraine-proxy.mrproper.dev/proxy/media"
    const val REUTERS_INVASION_MAP = "https://graphics.reuters.com/UKRAINE-CRISIS/zdpxokdxzvx/"
    var feed = "${getBaseUrl()}/feed"
    var map = "${getBaseUrl()}/map"

    private fun getBaseUrl() : String{
        if(BuildConfig.DEBUG && !ForceAccAPI){
            return BASE_URL_DEV
        }
        return BASE_URL_ACC
    }
}