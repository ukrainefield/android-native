package nl.gardensnakes.ukrainefield.view.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afdhal_fa.imageslider.ImageSlider
import com.afdhal_fa.imageslider.`interface`.ItemClickListener
import com.afdhal_fa.imageslider.model.SlideUIModel
import com.google.firebase.analytics.FirebaseAnalytics
import nl.gardensnakes.ukrainefield.MediaDetailActivity
import nl.gardensnakes.ukrainefield.R
import nl.gardensnakes.ukrainefield.data.remote.HttpRoutes
import nl.gardensnakes.ukrainefield.data.remote.dto.feed.FeedMessageResponse
import nl.gardensnakes.ukrainefield.helper.BookmarkHelper
import nl.gardensnakes.ukrainefield.helper.FirebaseHelper
import nl.gardensnakes.ukrainefield.helper.PreferenceHelper
import nl.gardensnakes.ukrainefield.helper.TimeHelper
import java.lang.Exception


class FeedCardAdapter(private var mList: List<FeedMessageResponse>, private val mFirebaseAnalytics: FirebaseAnalytics,  private val deleteOnUnBookmark: Boolean = false) :
    RecyclerView.Adapter<FeedCardAdapter.ViewHolder>() {

    lateinit var context: Context

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_card, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val feedData = mList[position]

        resetView(holder)

        holder.tagRecycleView!!.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.tagRecycleView!!.adapter = CategoryAdapter(feedData.categories)

        if (feedData.images.isNotEmpty()) {
            val imageList = ArrayList<SlideUIModel>()
            feedData.images.forEach {
                imageList.add(SlideUIModel("${HttpRoutes.MEDIA_PROXY}/$it", ""))
            }
            holder.imageSlide.setImageList(imageList)
            holder.imageSlide.setItemClickListener(object : ItemClickListener {
                override fun onItemClick(model: SlideUIModel, position: Int) {
                    val intent = Intent(context, MediaDetailActivity::class.java).apply {
                        putExtra("MEDIA_URL", "${HttpRoutes.MEDIA_PROXY}/${model.imageUrl}")
                        putExtra("MEDIA_TYPE", "image")
                    }
                    startActivity(context, intent, null)
                }
            })
        }
        holder.imageSlide.clipToOutline = true

        holder.titleView.text = getTitleText(feedData)
        holder.textView.text = feedData.text

        holder.postedAtText.text =
            "${context.getString(R.string.posted_at)} ${TimeHelper.epochToTimeString(feedData.epochTime.toLong())}"

        updateBookmarkText(feedData.messageURL ?: "", holder, position, false)

        if (feedData.videos.isEmpty() && feedData.images.isEmpty()) {
            holder.imageSlide.visibility = View.GONE
        } else if (feedData.videos.isNotEmpty()) {
            holder.imageSlide.visibility = View.GONE
            holder.videoView.visibility = View.VISIBLE
            holder.videoView.setVideoPath("${HttpRoutes.MEDIA_PROXY}/${feedData.videos[0]}")
            val mediaController = MediaController(context)
            mediaController.setAnchorView(holder.videoView)
            holder.videoView.setMediaController(mediaController)

            if (PreferenceHelper.shouldAutoPlayVideos(context)) {
                holder.videoView.start()
            }
            else{
                holder.videoView.seekTo(250)
            }

            holder.videoView.setOnClickListener {
                val intent = Intent(context, MediaDetailActivity::class.java).apply {
                    putExtra("MEDIA_URL", "${HttpRoutes.MEDIA_PROXY}/${feedData.videos[0]}")
                    putExtra("MEDIA_TYPE", "video")
                }
                startActivity(context, intent, null)
            }

        }

        if (feedData.messageURL == null) {
            holder.shareView.visibility = View.GONE
            holder.browserButtonView.visibility = View.GONE
        }

        holder.shareView.setOnClickListener {
            FirebaseHelper.logShareEvent(mFirebaseAnalytics, feedData.messageURL ?: "Unknown")
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, getTitleText(feedData))
            sendIntent.putExtra(Intent.EXTRA_TEXT, feedData.messageURL)
            sendIntent.type = "text/plain"
            startActivity(context, sendIntent, null)
        }

        holder.browserButtonView.setOnClickListener {
            FirebaseHelper.logOpenInBrowserEvent(mFirebaseAnalytics, feedData.messageURL ?: "Unkown")
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(feedData.messageURL))
            startActivity(context, browserIntent, null)
        }

        holder.bookmarkButton.setOnClickListener {
            BookmarkHelper().bookmark(feedData, context)
            updateBookmarkText(feedData.messageURL ?: "", holder, position)
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    private fun updateBookmarkText(messageUrl: String, holder: ViewHolder, position: Int, logEvent: Boolean = true){
        val isBookmarked = BookmarkHelper().isFavorite(messageUrl, context)
        if(isBookmarked){
            if(logEvent) {
                FirebaseHelper.logBookmarkEvent(mFirebaseAnalytics, messageUrl)
            }
            holder.bookmarkButton.text = context.getString(R.string.remove_bookmark)
        }
        else{
            if(logEvent) {
                FirebaseHelper.logUnbookmarkEvent(mFirebaseAnalytics, messageUrl)
            }
            holder.bookmarkButton.text = context.getString(R.string.bookmark)
            if(deleteOnUnBookmark){
                try {
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, mList.size)
                    mList = BookmarkHelper().getAll(context) ?: emptyList()
                    mList = mList.sortedByDescending { it.epochTime }
                } catch(e: Exception){}
            }
        }
    }

    private fun resetView(holder: ViewHolder) {
        holder.videoView.stopPlayback()
        holder.videoView.visibility = View.GONE
        holder.imageSlide.visibility = View.VISIBLE
    }

    private fun getTitleText(feedData: FeedMessageResponse): String {
        var title = "Message by: ${feedData.author}"
        if (feedData.type == "twitter") {
            title = "Tweet by: ${feedData.twitterData?.authorDisplayName ?: feedData.author}"
        } else if (feedData.type == "telegram") {
            title = "Telegram message by: ${feedData.telegramData?.authorName ?: feedData.author}"
        }
        return title
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageSlide: ImageSlider = itemView.findViewById(R.id.feed_card_thumbnail)
        val titleView: TextView = itemView.findViewById(R.id.feed_card_title)
        val textView: TextView = itemView.findViewById(R.id.feed_card_text)
        val postedAtText: TextView = itemView.findViewById(R.id.feed_card_posted_at_text)
        val shareView: Button = itemView.findViewById(R.id.feed_card_share_button)
        val browserButtonView: Button = itemView.findViewById(R.id.feed_card_browser_button)
        val videoView: VideoView = itemView.findViewById(R.id.feed_card_video)
        var bookmarkButton: Button = ItemView.findViewById(R.id.feed_card_bookmark_button)
        val tagRecycleView: RecyclerView = ItemView.findViewById(R.id.feed_tag_recycle_view)
    }
}