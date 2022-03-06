package nl.gardensnakes.ukrainefield.helper

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import nl.gardensnakes.ukrainefield.R

class SwipeRefreshHelper {
    companion object {
        fun setupColors(refreshLayout: SwipeRefreshLayout, context: Context){
            refreshLayout.setColorSchemeColors(ContextCompat.getColor(context, R.color.ukraineAzure), ContextCompat.getColor(context, R.color.ukraineYellow));
        }
    }
}