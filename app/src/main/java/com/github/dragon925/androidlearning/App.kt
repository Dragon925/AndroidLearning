package com.github.dragon925.androidlearning

import android.app.Application
import androidx.appcompat.content.res.AppCompatResources
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.asImage
import coil3.request.CachePolicy
import coil3.request.crossfade

class App : Application(), SingletonImageLoader.Factory {

    override fun newImageLoader(context: PlatformContext) = ImageLoader.Builder(context)
        .crossfade(true)
        .diskCachePolicy(CachePolicy.DISABLED)
        .memoryCachePolicy(CachePolicy.DISABLED)
        .placeholder(
            AppCompatResources.getDrawable(this, R.drawable.img_placeholder)?.asImage()
        )
        .error(
            AppCompatResources.getDrawable(this, R.drawable.img_placeholder)?.asImage()
        )
        .build()
}