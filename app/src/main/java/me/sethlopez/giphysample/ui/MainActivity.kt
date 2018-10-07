package me.sethlopez.giphysample.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import me.sethlopez.giphysample.R
import me.sethlopez.giphysample.data.NetworkState
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {
    lateinit var mediaAdapter: MediaAdapter
    lateinit var viewModel: MainActivityViewModel
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel::class.java)
        // initialize the SwipeRefreshLayout
        refreshLayout.setOnRefreshListener { viewModel.refresh() }
        // initialize the RecyclerView
        mediaAdapter = MediaAdapter()
        recyclerview.apply {
            adapter = mediaAdapter
            layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        }
        // observe data changes in the ViewModel
        viewModel.apply {
            query.observe(this@MainActivity, Observer { toolbarTitle.text = it })
            list.observe(this@MainActivity, Observer { mediaAdapter.submitList(it) })
            initialLoadState.observe(this@MainActivity, Observer { networkState ->
                refreshLayout.isRefreshing = networkState == NetworkState.Loading
            })
        }
    }
}
