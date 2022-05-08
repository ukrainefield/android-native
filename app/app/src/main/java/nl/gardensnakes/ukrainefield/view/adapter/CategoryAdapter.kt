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
import com.afdhal_fa.imageslider.ImageSlider
import com.afdhal_fa.imageslider.`interface`.ItemClickListener
import com.afdhal_fa.imageslider.model.SlideUIModel
import com.google.android.material.chip.Chip
import com.google.firebase.analytics.FirebaseAnalytics
import nl.gardensnakes.ukrainefield.MediaDetailActivity
import nl.gardensnakes.ukrainefield.R
import nl.gardensnakes.ukrainefield.data.remote.HttpRoutes
import nl.gardensnakes.ukrainefield.data.remote.dto.feed.FeedMessageResponse
import nl.gardensnakes.ukrainefield.helper.*
import java.lang.Exception


class CategoryAdapter(private var mList: List<String>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    lateinit var context: Context

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tag_chip, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val chipText = mList[position]

        holder.chip.text = StringHelper.capitalizeWords(chipText)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val chip: Chip = ItemView.findViewById(R.id.tag_chip)
    }
}