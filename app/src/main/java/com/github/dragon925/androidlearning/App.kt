package com.github.dragon925.androidlearning

import android.app.Application
import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.asImage
import coil3.request.CachePolicy
import coil3.request.crossfade
import com.github.dragon925.androidlearning.common.di.AppComponent
import com.github.dragon925.androidlearning.common.di.DaggerAppComponent
import com.github.dragon925.androidlearning.common.ui.DepsHandler
import com.github.dragon925.androidlearning.core.api.R as CoreR
import com.github.dragon925.androidlearning.core.api.domain.repositories.CategoryRepository
import com.github.dragon925.androidlearning.core.api.domain.repositories.EventRepository
import com.github.dragon925.androidlearning.core.di.CoreComponent
import com.github.dragon925.androidlearning.core.di.DaggerCoreComponent

class App : Application(), SingletonImageLoader.Factory, DepsHandler {

    lateinit var appComponent: AppComponent
    private lateinit var coreComponent: CoreComponent

    override val eventRepository: EventRepository
        get() = coreComponent.eventRepository

    override val categoryRepository: CategoryRepository
        get() = coreComponent.categoryRepository

    override val context: Context
        get() = this

    override fun newImageLoader(context: PlatformContext) = ImageLoader.Builder(context)
        .crossfade(true)
        .diskCachePolicy(CachePolicy.DISABLED)
        .memoryCachePolicy(CachePolicy.DISABLED)
        .placeholder(
            AppCompatResources.getDrawable(this, CoreR.drawable.img_placeholder)?.asImage()
        )
        .error(
            AppCompatResources.getDrawable(this, CoreR.drawable.img_placeholder)?.asImage()
        )
        .build()

    override fun onCreate() {
        super.onCreate()
        coreComponent = DaggerCoreComponent.builder()
            .context(this)
            .build()
        appComponent = DaggerAppComponent.builder()
            .context(this)
            .coreComponent(coreComponent)
            .build()
    }
}