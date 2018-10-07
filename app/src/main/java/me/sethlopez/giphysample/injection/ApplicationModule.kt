package me.sethlopez.giphysample.injection

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import me.sethlopez.giphysample.ui.MainActivity

@Module(includes = [AndroidSupportInjectionModule::class])
abstract class ApplicationModule {
    @PerActivity
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun contributeMainActivityInjector(): MainActivity
}