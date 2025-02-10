package com.github.dragon925.androidlearning.ui

import android.content.Context

fun Context.dpToPx(dp: Int): Int = (dp * resources.displayMetrics.density).toInt()
