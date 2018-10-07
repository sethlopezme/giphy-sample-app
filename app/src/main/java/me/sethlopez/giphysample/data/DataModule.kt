package me.sethlopez.giphysample.data

import com.giphy.sdk.core.network.api.GPHApiClient
import dagger.Module
import dagger.Provides
import me.sethlopez.giphysample.BuildConfig
import me.sethlopez.giphysample.injection.PerApplication

@Module
class DataModule {
    @Provides
    @PerApplication
    fun provideGiphyClient(): GPHApiClient = GPHApiClient(BuildConfig.GIPHY_API_KEY)
}