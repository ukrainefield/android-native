package nl.gardensnakes.ukrainefield

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.coroutines.*
import nl.gardensnakes.ukrainefield.data.remote.FeedService
import nl.gardensnakes.ukrainefield.data.remote.SavedPreferences
import nl.gardensnakes.ukrainefield.helper.FirebaseHelper
import nl.gardensnakes.ukrainefield.helper.NewsFeedHelper
import nl.gardensnakes.ukrainefield.helper.SwipeRefreshHelper
import nl.gardensnakes.ukrainefield.view.adapter.FeedCardAdapter


class NewsFeedFragment : Fragment() {

    private val feedService: FeedService = FeedService.create()
    private var scope = CoroutineScope(Job() + Dispatchers.Main)
    private var jobs: MutableList<Job> = mutableListOf()

    private lateinit var feedRecyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var feedCardAdapter: FeedCardAdapter
    private var useProxyServer: Boolean = false
    private lateinit var mFirebaseAnalytics: FirebaseAnalytics

    private lateinit var noNewsText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_newsfeed, container, false)

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(view.context);
        FirebaseHelper.updateCurrentScreen(mFirebaseAnalytics, this.requireActivity(), this.javaClass.simpleName, this.javaClass.simpleName)

        useProxyServer = SavedPreferences.useProxyServer(requireContext())

        swipeRefreshLayout = view.findViewById(R.id.newsfeed_refresh_layout)
        feedRecyclerView = view.findViewById(R.id.newsfeed_recycle_view)
        noNewsText = view.findViewById(R.id.feed_no_news_found)

        feedCardAdapter = FeedCardAdapter(emptyList(), mFirebaseAnalytics)
        feedRecyclerView.adapter = feedCardAdapter

        setupRefreshLayout()
        getFeedData()

        return view;
    }

    private fun setupRefreshLayout() {
        SwipeRefreshHelper.setupColors(swipeRefreshLayout, requireContext())
        swipeRefreshLayout.setOnRefreshListener {
            getFeedData()
        }
    }

    private fun getFeedData() {
        swipeRefreshLayout.isRefreshing = true
        var job = scope.launch {
            val feedData = feedService.getAllFeed()
            swipeRefreshLayout.isRefreshing = false
            if(feedData != null && context != null) {
                var filteredNews = NewsFeedHelper.filterSourcesBasedOnPreferences(feedData.messages, requireContext())
                if(filteredNews.isEmpty()){
                    noNewsText.visibility = View.VISIBLE
                    feedRecyclerView.visibility = View.GONE
                }
                else {
                    feedCardAdapter = FeedCardAdapter(filteredNews, mFirebaseAnalytics)
                    feedRecyclerView.adapter = feedCardAdapter
                    feedRecyclerView.layoutManager = LinearLayoutManager(requireView().context);
                }
            }
        }
        jobs.add(job)
    }

    override fun onResume() {
        super.onResume()
        scope = CoroutineScope(Job() + Dispatchers.Main)
        if(feedCardAdapter.itemCount == 0) {
            getFeedData()
        }
    }

    override fun onPause() {
        super.onPause()
        cleanUp()
    }

    private fun cleanUp(){
        scope.cancel()
        jobs.forEach {
            try {
                it.cancel("View closed")
            } catch(e: Exception){}
        }
        jobs = mutableListOf()
    }
}