package nl.gardensnakes.ukrainefield

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import nl.gardensnakes.ukrainefield.data.remote.FeedService
import nl.gardensnakes.ukrainefield.data.remote.MapService
import nl.gardensnakes.ukrainefield.data.remote.SavedPreferences
import nl.gardensnakes.ukrainefield.view.adapter.FeedCardAdapter
import nl.gardensnakes.ukrainefield.view.adapter.MapCardAdapter

class MapFragment : Fragment() {

    private val feedService: MapService = MapService.create()
    private var scope = CoroutineScope(Job() + Dispatchers.Main)
    private var jobs: MutableList<Job> = mutableListOf()

    private lateinit var mapRecyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mapCardAdapter: MapCardAdapter
    private var useProxyServer: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        useProxyServer = SavedPreferences.useProxyServer(requireContext())

        swipeRefreshLayout = view.findViewById(R.id.map_refresh_layout)
        mapRecyclerView = view.findViewById(R.id.map_recycle_view)

        mapCardAdapter = MapCardAdapter(emptyList())
        mapRecyclerView.adapter = mapCardAdapter

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
            val feedData = feedService.getAllMaps()
            swipeRefreshLayout.isRefreshing = false
            if(feedData != null) {
                mapCardAdapter = MapCardAdapter(feedData.mapData)
                mapRecyclerView.adapter = mapCardAdapter
                mapRecyclerView.layoutManager = LinearLayoutManager(requireView().context);
            }
        }
        jobs.add(job)
    }
}