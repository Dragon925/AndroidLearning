package com.github.dragon925.androidlearning.common.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable

fun Context.dpToPx(dp: Int): Int = (dp * resources.displayMetrics.density).toInt()

fun Context.getAssetDrawable(path: String): Drawable? = try {
    assets.open(path).use { inputStream ->
        Drawable.createFromStream(inputStream, null)
    }
} catch (e: Exception) {
    null
}

@Suppress("DEPRECATION")
inline fun <reified T: Parcelable> Bundle.readParcelableArrayList(key: String): ArrayList<T>? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getParcelableArrayList(key, T::class.java)
    } else {
        this.getParcelableArrayList(key)
    }
}

@Suppress("DEPRECATION")
inline fun <reified T: Parcelable> Bundle.readParcelable(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getParcelable(key, T::class.java)
    } else {
        this.getParcelable(key)
    }
}