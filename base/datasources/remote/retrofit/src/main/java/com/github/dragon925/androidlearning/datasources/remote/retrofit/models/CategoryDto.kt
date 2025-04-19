package com.github.dragon925.androidlearning.datasources.remote.retrofit.models

import com.github.dragon925.androidlearning.datasources.contract.CategoryContract
import com.google.gson.annotations.SerializedName

internal data class CategoryDto(
    override val id: String,
    override val name: String,
    @SerializedName("name_en") override val nameEn: String = "",
    override val image: String = ""
): CategoryContract
