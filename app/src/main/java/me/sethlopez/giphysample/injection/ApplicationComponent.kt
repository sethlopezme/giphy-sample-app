package me.sethlopez.giphysample.injection

import dagger.Component
import dagger.android.AndroidInjector
import me.sethlopez.giphysample.GiphySampleApplication
import me.sethlopez.giphysample.data.DataModule
import javax.inject.Scope

@PerApplication
@Component(modules = [ApplicationModule::class, DataModule::class])
interface ApplicationComponent : AndroidInjector<GiphySampleApplication> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<GiphySampleApplication>()
}

@Scope
annotation class PerApplication

@Scope
annotation class PerActivity