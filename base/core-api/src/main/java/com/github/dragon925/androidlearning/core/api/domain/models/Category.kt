package com.github.dragon925.androidlearning.core.api.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val id: String,
    val name: String,
    val url: String
) : Parcelable
