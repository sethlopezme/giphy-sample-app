package me.sethlopez.giphysample.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.MapKey
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

/**
 * Factory for creating and injecting ViewModels via Dagger.
 * See: https://git.io/flbm0
 */
class ViewModelInjectionFactory @Inject constructor(
    private val providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(viewModelClass: Class<T>): T {
        val creator = providers[viewModelClass]
            ?: providers.entries.firstOrNull { viewModelClass.isAssignableFrom(it.key) }?.value
            ?: throw IllegalArgumentException("unknown ViewModel class $viewModelClass")
        try {
            @Suppress("UNCHECKED_CAST")
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}

/**
 * Map key for ViewModelInjectionFactory.
 */
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)