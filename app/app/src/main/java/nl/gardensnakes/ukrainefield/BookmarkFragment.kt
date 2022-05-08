package nl.gardensnakes.ukrainefield

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import nl.gardensnakes.ukrainefield.data.remote.SavedPreferences
import nl.gardensnakes.ukrainefield.helper.BookmarkHelper
import nl.gardensnakes.ukrainefield.helper.FirebaseHelper
import nl.gardensnakes.ukrainefield.helper.NewsFeedHelper
import nl.gardensnakes.ukrainefield.view.adapter.FeedCardAdapter

class BookmarkFragment : Fragment() {

    private lateinit var feedRecyclerView: RecyclerView
    private lateinit var noBookmarksText: TextView

    private lateinit var feedCardAdapter: FeedCardAdapter
    private var useProxyServer: Boolean = false
    private lateinit var mFirebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bookmark, container, false)

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(view.context);
        FirebaseHelper.updateCurrentScreen(mFirebaseAnalytics, this.requireActivity(), this.javaClass.simpleName, this.javaClass.simpleName)

        useProxyServer = SavedPreferences.useProxyServer(requireContext())
        feedRecyclerView = view.findViewById(R.id.bookmark_recycler_view)
        noBookmarksText = view.findViewById(R.id.bookmark_no_bookmark_text)

        val bookmarkedItems = BookmarkHelper().getAll(view.context) ?: emptyList()
        val filteredBookmarks = NewsFeedHelper.filterSourcesBasedOnPreferences(bookmarkedItems.sortedByDescending { it.epochTime}, requireContext())
        if(filteredBookmarks.isEmpty()){
            noBookmarksText.visibility = View.VISIBLE
            feedRecyclerView.visibility = View.GONE
        }
        else {
            noBookmarksText.visibility = View.GONE
            feedRecyclerView.visibility = View.VISIBLE
            feedCardAdapter = FeedCardAdapter(filteredBookmarks, mFirebaseAnalytics, true)
            feedRecyclerView.adapter = feedCardAdapter
            feedRecyclerView.layoutManager = LinearLayoutManager(view.context);
        }

        return view
    }
}