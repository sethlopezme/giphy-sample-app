package me.sethlopez.giphysample.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PositionalDataSource
import com.giphy.sdk.core.models.Media
import com.giphy.sdk.core.models.enums.LangType
import com.giphy.sdk.core.models.enums.MediaType
import com.giphy.sdk.core.models.enums.RatingType
import com.giphy.sdk.core.network.api.GPHApiClient
import java.util.*

/**
 * Requests data from the Giphy API for the given query on an as-needed basis.
 */
class GiphyDataSource(
    private val client: GPHApiClient,
    val query: String
) : PositionalDataSource<Media>() {
    // provide the state of network requests to all interested observers
    val networkStateLiveData = MutableLiveData<NetworkState>()
    val initialLoadStateLiveData = MutableLiveData<NetworkState>()
    // provide a reference to a function that will retry the last failed request
    var retry: (() -> Unit)? = null

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Media>) {
        // notify observers that a network request is in progress
        networkStateLiveData.postValue(NetworkState.Loading)
        initialLoadStateLiveData.postValue(NetworkState.Loading)
        // perform the request
        client.search(
            query,
            MediaType.gif,
            params.requestedLoadSize,
            params.requestedStartPosition,
            RatingType.pg,
            LangType.english
        ) { response, error ->
            if (response == null) {
                // provide a retry handler to load the initial data
                retry = { loadInitial(params, callback) }
                val cause = error ?: RuntimeException("no response received")
                val state = NetworkState.Failure(cause, cause.message)
                // notify observers that the network request has failed
                networkStateLiveData.postValue(state)
                initialLoadStateLiveData.postValue(state)
            } else {
                retry = null
                // provide the initial data to the UI
                response.data?.also { data -> callback.onResult(data, response.pagination.offset) }
                // notify observers that the network request has succeeded
                networkStateLiveData.postValue(NetworkState.Success)
                initialLoadStateLiveData.postValue(NetworkState.Success)
            }
        }
    }

    override fun loadRange(
        params: PositionalDataSource.LoadRangeParams,
        callback: PositionalDataSource.LoadRangeCallback<Media>
    ) {
        // notify observers that a network request is in progress
        networkStateLiveData.postValue(NetworkState.Loading)
        // perform the request
        client.search(
            query,
            MediaType.gif,
            params.loadSize,
            params.startPosition,
            RatingType.pg,
            LangType.english
        ) { response, error ->
            if (response == null) {
                // provide a retry handler to load subsequent data
                retry = { loadRange(params, callback) }
                val cause = error ?: RuntimeException("no response received")
                // notify observers that the network request has failed
                networkStateLiveData.postValue(NetworkState.Failure(cause, cause.message))
            } else {
                retry = null
                // provide the data to the UI
                response.data?.also { data -> callback.onResult(data) }
                // notify observers that the network request has succeeded
                networkStateLiveData.postValue(NetworkState.Success)
            }
        }
    }

    /**
     * Creates new instances of a GiphyDataSource that will make requests for the given query.
     */
    class Factory(
        private val client: GPHApiClient
    ) : DataSource.Factory<Int, Media>() {
        val dataSourceLiveData = MutableLiveData<GiphyDataSource>()

        override fun create(): DataSource<Int, Media> {
            val randomQuery = QUERIES[Random().nextInt(QUERIES.size)]
            val dataSource = GiphyDataSource(client, randomQuery)
            dataSourceLiveData.postValue(dataSource)
            return dataSource
        }

        companion object {
            @JvmStatic
            val QUERIES = listOf(
                "high five",
                "clapping",
                "thumbs up",
                "mic drop",
                "laughing",
                "waiting",
                "yes",
                "no",
                "nope",
                "confused",
                "shrug"
            )
        }
    }
}