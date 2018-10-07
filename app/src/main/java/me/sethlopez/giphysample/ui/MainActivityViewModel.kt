package me.sethlopez.giphysample.ui

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.core.network.api.GPHApiClient
import me.sethlopez.giphysample.data.GiphyDataSource
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    giphyClient: GPHApiClient
) : ViewModel() {
    private val dataSourceFactory = GiphyDataSource.Factory(giphyClient)
    val initialLoadState = Transformations.map(dataSourceFactory.dataSourceLiveData) { dataSource ->
        dataSource.initialLoadStateLiveData.value
    }
    val query = Transformations.map(dataSourceFactory.dataSourceLiveData) { dataSource -> "#${dataSource.query}" }
    val list = LivePagedListBuilder<Int, Media>(
        dataSourceFactory,
        PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(40)
            .setPageSize(20)
            .setPrefetchDistance(10)
            .build()
    ).build()

    // create a new data source
    fun refresh() = dataSourceFactory.dataSourceLiveData.value?.invalidate()
}