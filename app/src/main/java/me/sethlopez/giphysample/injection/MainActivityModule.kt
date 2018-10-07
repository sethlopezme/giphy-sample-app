package me.sethlopez.giphysample.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import me.sethlopez.giphysample.ui.MainActivityViewModel

@Module
abstract class MainActivityModule {
    @Binds
    abstract fun bindViewModelInjectionFactory(factory: ViewModelInjectionFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainActivityViewModel(viewModel: MainActivityViewModel): ViewModel
}