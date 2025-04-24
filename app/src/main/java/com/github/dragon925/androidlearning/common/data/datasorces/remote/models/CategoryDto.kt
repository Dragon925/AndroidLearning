package com.github.dragon925.androidlearning.common.data.datasorces.remote.models

import com.github.dragon925.androidlearning.common.contract.CategoryContract
import com.google.gson.annotations.SerializedName

data class CategoryDto(
    override val id: String,
    override val name: String,
    @SerializedName("name_en") override val nameEn: String = "",
    override val image: String = ""
): CategoryContract
