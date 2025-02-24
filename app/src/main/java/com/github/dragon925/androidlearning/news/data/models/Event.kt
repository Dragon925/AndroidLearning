package com.github.dragon925.androidlearning.news.data.models

import com.google.gson.annotations.SerializedName
import kotlinx.datetime.LocalDate

data class Event(
    val id: Int,
    val name: String,
    val description: String,
    val organizer: String,

    @SerializedName(CATEGORY_IDS)
    val categoryIds: List<Int>,

    val address: String,

    @SerializedName(PHONE_NUMBERS)
    val phoneNumbers: List<String>,

    val email: String,
    val website: String,

    @SerializedName(START_DATE)
    val startDate: LocalDate,

    @SerializedName(END_DATE)
    val endDate: LocalDate,

    val photos: List<String>,
    val members: List<Member> = emptyList()
) {
    companion object {
        const val ID = "id"
        const val NAME = "name"
        const val DESCRIPTION = "description"
        const val ORGANIZER = "organizer"
        const val CATEGORY_IDS = "category_ids"
        const val ADDRESS = "address"
        const val PHONE_NUMBERS = "phone_numbers"
        const val EMAIL = "email"
        const val WEBSITE = "website"
        const val START_DATE = "start_date"
        const val END_DATE = "end_date"
        const val PHOTOS = "photos"
        const val MEMBERS = "members"
    }
}
