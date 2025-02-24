package com.github.dragon925.androidlearning.common.domain

import android.os.Parcel
import android.os.Parcelable
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
) : Parcelable {

    companion object CREATOR : Parcelable.Creator<Event> {
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

        override fun createFromParcel(parcel: Parcel): Event = Event(parcel)
        override fun newArray(size: Int): Array<Event?> = arrayOfNulls(size)

        private val dateFormat = LocalDate.Format {
            year(); chars("-"); monthNumber(); chars("-"); dayOfMonth()
        }
    }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createIntArray()?.toList() ?: emptyList(),
        parcel.readString() ?: "",
        parcel.createStringArray()?.toList() ?: emptyList(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        dateFormat.parse(parcel.readString() ?: ""),
        dateFormat.parse(parcel.readString() ?: ""),
        parcel.createStringArray()?.toList() ?: emptyList(),
        parcel.createTypedArray(Member.CREATOR)?.toList() ?: emptyList()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(organizer)
        parcel.writeIntArray(categoryIds.toIntArray())
        parcel.writeString(address)
        parcel.writeStringArray(phoneNumbers.toTypedArray())
        parcel.writeString(email)
        parcel.writeString(website)
        parcel.writeString(dateFormat.format(startDate))
        parcel.writeString(dateFormat.format(endDate))
        parcel.writeStringArray(photos.toTypedArray())
        parcel.writeTypedArray(members.toTypedArray(), flags)
    }

    override fun describeContents(): Int = 0
}
