package com.github.dragon925.androidlearning

import android.content.Context

fun Context.dpToPx(dp: Int): Int = (dp * resources.displayMetrics.density).toInt()
