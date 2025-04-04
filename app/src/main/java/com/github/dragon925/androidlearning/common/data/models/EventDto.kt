package com.github.dragon925.androidlearning.common.data.models

import com.google.gson.annotations.SerializedName

data class EventDto(
    val id: String,
    val name: String,
    val startDate: Long,
    val endDate: Long,
    val description: String,
    val status: Long,
    val photos: List<String>,

    @SerializedName("category")
    val categories: List<String>,

    val createAt: Long,
    val phone: String,
    val address: String,

    @SerializedName("organisation")
    val organization: String
)
