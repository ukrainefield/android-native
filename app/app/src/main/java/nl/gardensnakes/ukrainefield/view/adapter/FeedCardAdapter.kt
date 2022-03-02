package nl.gardensnakes.ukrainefield.view.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import nl.gardensnakes.ukrainefield.R
import nl.gardensnakes.ukrainefield.data.remote.dto.FeedMessageResponse


class FeedCardAdapter(private val mList: List<FeedMessageResponse>) : RecyclerView.Adapter<FeedCardAdapter.ViewHolder>() {

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

        try {

            Glide.with(holder.imageView)
                .load(feedData.images[0])
                .into(holder.imageView)
        } catch (E: Exception){}
        holder.titleView.text = getTitleText(feedData)
        holder.textView.text = feedData.text

        if(feedData.videos.isEmpty() && feedData.images.isEmpty()){
            holder.imageView.visibility = View.GONE
        }
        else if(feedData.videos.isNotEmpty()) {
            holder.imageView.visibility = View.GONE
            holder.videoView.visibility = View.VISIBLE
            holder.videoView.setVideoPath(feedData.videos[0])
            val mediaController = MediaController(context)
            mediaController.setAnchorView(holder.videoView)
            holder.videoView.setMediaController(mediaController)
            holder.videoView.start()
        }

        if(feedData.messageURL == null){
            holder.shareView.visibility = View.GONE
            holder.browserButtonView.visibility = View.GONE
        }

        holder.shareView.setOnClickListener{
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, getTitleText(feedData));
            sendIntent.putExtra(Intent.EXTRA_TEXT, feedData.messageURL)
            sendIntent.type = "text/plain"
            startActivity(context, sendIntent, null)
        }

        holder.browserButtonView.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(feedData.messageURL))
            startActivity(context, browserIntent,null)
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    private fun getTitleText(feedData: FeedMessageResponse): String{
        var title =  "Message by: ${feedData.author}"
        if(feedData.type == "twitter"){
            title = "Tweet by: ${feedData.twitterData?.authorDisplayName ?: feedData.author}"
        }
        else if(feedData.type == "telegram"){
            title = "Telegram message by: ${feedData.telegramData?.authorName ?: feedData.author}"
        }
        return title
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.feed_card_thumbnail)
        val titleView: TextView = itemView.findViewById(R.id.feed_card_title)
        val textView: TextView = itemView.findViewById(R.id.feed_card_text)
        val shareView: Button = itemView.findViewById(R.id.feed_card_share_button)
        val browserButtonView: Button = itemView.findViewById(R.id.feed_card_browser_button)
        val videoView: VideoView = itemView.findViewById(R.id.feed_card_video)
    }
}