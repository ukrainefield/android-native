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
import com.github.piasy.biv.BigImageViewer
import com.github.piasy.biv.loader.glide.GlideImageLoader
import com.github.piasy.biv.view.BigImageView
import nl.gardensnakes.ukrainefield.MediaDetailActivity
import nl.gardensnakes.ukrainefield.R
import nl.gardensnakes.ukrainefield.data.remote.HttpRoutes
import nl.gardensnakes.ukrainefield.data.remote.dto.feed.FeedMessageResponse
import nl.gardensnakes.ukrainefield.data.remote.dto.map.MapDataResponse


class MapCardAdapter(private val mList: List<MapDataResponse>) : RecyclerView.Adapter<MapCardAdapter.ViewHolder>() {

    lateinit var context: Context
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        context = parent.context
        BigImageViewer.initialize(GlideImageLoader.with(context));
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.map_card, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val mapData = mList[position]

        resetView(holder)

        holder.thumbnail.showImage(Uri.parse("${HttpRoutes.MEDIA_PROXY}/${mapData.imagePath}"))
        holder.thumbnail.clipToOutline = true

        holder.title.text = "Reuters Invasion Map | Updated ${mapData.displayUpdatedTime}"
        holder.description.text = mapData.messageText
        holder.shareButton.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, mapData.messageLink)
            sendIntent.type = "text/plain"
            startActivity(context, sendIntent, null)
        }

        holder.thumbnail.setOnClickListener {
            val intent = Intent(context, MediaDetailActivity::class.java).apply {
                putExtra("MEDIA_URL", mapData.imagePath)
                putExtra("MEDIA_TYPE", "image")
            }
            startActivity(context, intent, null)
        }

        holder.articleButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mapData.messageLink))
            startActivity(context, browserIntent,null)
        }

        holder.mapButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(HttpRoutes.REUTERS_INVASION_MAP))
            startActivity(context, browserIntent,null)
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    private fun resetView(holder: ViewHolder){

    }


    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var thumbnail: BigImageView = ItemView.findViewById(R.id.map_card_thumbnail)
        var title: TextView = ItemView.findViewById(R.id.map_card_title)
        var description: TextView = ItemView.findViewById(R.id.map_card_text)
        var shareButton: Button = ItemView.findViewById(R.id.map_card_share_button)
        var articleButton: Button = ItemView.findViewById(R.id.map_card_open_article_button)
        var mapButton: Button = ItemView.findViewById(R.id.map_card_open_card_button)

    }
}