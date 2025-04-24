package com.github.dragon925.androidlearning.common.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Member(
    val id: Int,
    val name: String,
    val avatar: String,
) : Parcelable
