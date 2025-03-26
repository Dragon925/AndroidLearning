package com.github.dragon925.androidlearning.common.data.models

import com.google.gson.annotations.SerializedName

data class CategoryDto(
    val id: String,
    val name: String,
    @SerializedName("name_en") val nameEn: String = "",
    val image: String = ""
)
