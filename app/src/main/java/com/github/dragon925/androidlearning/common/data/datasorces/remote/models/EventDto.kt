package com.github.dragon925.androidlearning.common.data.datasorces.remote.models

import com.github.dragon925.androidlearning.common.contract.EventContract
import com.google.gson.annotations.SerializedName

data class EventDto(
    override val id: String,
    override val name: String,
    override val startDate: Long,
    override val endDate: Long,
    override val description: String,
    override val status: Long,
    override val photos: List<String>,

    @SerializedName("category")
    override val categories: List<String>,

    override val createAt: Long,
    override val phone: String,
    override val address: String,

    @SerializedName("organisation")
    override val organization: String
) : EventContract
