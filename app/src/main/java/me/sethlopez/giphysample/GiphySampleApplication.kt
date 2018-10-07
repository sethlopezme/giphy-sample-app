package me.sethlopez.giphysample

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import me.sethlopez.giphysample.injection.DaggerApplicationComponent

class GiphySampleApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerApplicationComponent.builder().create(this)
}