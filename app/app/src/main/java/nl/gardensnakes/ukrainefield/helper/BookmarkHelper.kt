package nl.gardensnakes.ukrainefield.helper

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import nl.gardensnakes.ukrainefield.data.remote.dto.feed.FeedMessageResponse
import java.io.*

class BookmarkHelper {
    private var bookmarkedFeed: ArrayList<FeedMessageResponse>? = null

    fun bookmark(feedItem: FeedMessageResponse, context: Context) {
        val feedMessages = getAll(context) as ArrayList<FeedMessageResponse>

        val found = feedMessages.firstOrNull {
            it.messageURL == feedItem.messageURL
        }

        if(found == null) {
            feedMessages.add(feedItem)
        } else {
            feedMessages.remove(found)
        }

        val json = extractToJson(feedMessages);

        if(json != null) saveToJsonFile(json, context)
    }

    fun resetBookmarks(context: Context){
        saveToJsonFile("", context)
    }


    fun isFavorite(messageUrl: String, context: Context): Boolean {
        return getAll(context)?.any {
            it.messageURL == messageUrl
        } ?: false
    }

    fun getAll(context: Context?): List<FeedMessageResponse>? {
        return if(bookmarkedFeed != null) {
            return bookmarkedFeed
        } else {
            context?.let {
                readJsonFile(it)
            }?.let {
                extractToBookmark(it)
            }
        }
    }

    private fun extractToBookmark(videos: String): List<FeedMessageResponse>? {
        val listOfBookmarks = Gson().fromJson<List<FeedMessageResponse>>(
            videos,
            object : TypeToken<ArrayList<FeedMessageResponse>>() {}.type
        ) ?: ArrayList(0)

        this.bookmarkedFeed = listOfBookmarks as ArrayList<FeedMessageResponse>

        return listOfBookmarks
    }

    private fun extractToJson(bookmarks: List<FeedMessageResponse>): String? {
        return Gson().toJson(
            bookmarks,
            List::class.java
        )
    }

    private fun saveToJsonFile(json: String, context: Context) {
        try {
            val outputStreamWriter = OutputStreamWriter(
                context.openFileOutput(
                    "bookmarks.txt",
                    Context.MODE_PRIVATE
                )
            )

            outputStreamWriter.write(json)
            outputStreamWriter.close()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: $e")
        }
    }

    private fun readJsonFile(context: Context): String {
        var ret = ""

        try {
            val inputStream: InputStream? = context.openFileInput("bookmarks.txt")
            if (inputStream != null) {
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                var receiveString: String? = ""
                val stringBuilder = StringBuilder()
                while (bufferedReader.readLine().also { receiveString = it } != null) {
                    stringBuilder.append("\n").append(receiveString)
                }
                inputStream.close()
                ret = stringBuilder.toString()
            }
        } catch (e: FileNotFoundException) {
            Log.e("BookmarkHelper", "File not found: $e")
        } catch (e: IOException) {
            Log.e("BookmarkHelper", "Can not read file: $e")
        }

        return ret
    }
}