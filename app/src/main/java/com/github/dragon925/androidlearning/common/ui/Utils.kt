package com.github.dragon925.androidlearning.common.ui

import android.content.Context
import android.graphics.drawable.Drawable

fun Context.dpToPx(dp: Int): Int = (dp * resources.displayMetrics.density).toInt()

fun Context.getAssetDrawable(path: String): Drawable? = try {
    assets.open(path).use { inputStream ->
        Drawable.createFromStream(inputStream, null)
    }
} catch (e: Exception) {
    null
}
