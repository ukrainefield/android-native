package nl.gardensnakes.ukrainefield

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.*
import nl.gardensnakes.ukrainefield.data.remote.FeedService
import nl.gardensnakes.ukrainefield.data.remote.dto.FeedMessageResponse
import nl.gardensnakes.ukrainefield.view.adapter.FeedCardAdapter

class NewsFeedFragment : Fragment() {

    private val feedService: FeedService = FeedService.create()
    private var scope = CoroutineScope(Job() + Dispatchers.Main)
    private var jobs: MutableList<Job> = mutableListOf()

    private lateinit var feedRecyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var feedCardAdapter: FeedCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_newsfeed, container, false)

        swipeRefreshLayout = view.findViewById(R.id.newsfeed_refresh_layout)
        feedRecyclerView = view.findViewById(R.id.newsfeed_recycle_view)

        feedCardAdapter = FeedCardAdapter(emptyList())
        feedRecyclerView.adapter = feedCardAdapter

        setupRefreshLayout()
        getFeedData()

        return view;
    }

    private fun setupRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener {
            getFeedData()
        }
    }

    private fun getFeedData() {
        swipeRefreshLayout.isRefreshing = true
        var job = scope.launch {
            val feedData = feedService.getAllFeed()
            swipeRefreshLayout.isRefreshing = false
            if(feedData != null) {
                feedCardAdapter = FeedCardAdapter(feedData.messages)
                feedRecyclerView.adapter = feedCardAdapter
                feedRecyclerView.layoutManager = LinearLayoutManager(requireView().context);
            }
        }
        jobs.add(job)
    }

    override fun onResume() {
        super.onResume()
        scope = CoroutineScope(Job() + Dispatchers.Main)
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