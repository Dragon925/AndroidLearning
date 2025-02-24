package com.github.dragon925.androidlearning.news.ui.models


import android.os.Parcel
import android.os.Parcelable
import com.github.dragon925.androidlearning.common.domain.Member

data class NewsDetailItem(
    val id: Int,
    val name: String,
    val description: String,
    val organizer: String,
    val address: String,
    val phoneNumbers: List<String>,
    val email: String,
    val website: String,
    val date: String,
    val photos: List<String>,
    val members: List<Member> = emptyList()
) : Parcelable {

    companion object CREATOR : Parcelable.Creator<NewsDetailItem> {
        override fun createFromParcel(parcel: Parcel): NewsDetailItem = NewsDetailItem(parcel)
        override fun newArray(size: Int): Array<NewsDetailItem?> = arrayOfNulls(size)
    }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArray()?.toList() ?: emptyList(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArray()?.toList() ?: emptyList(),
        parcel.createTypedArray(Member.CREATOR)?.toList() ?: emptyList(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(organizer)
        parcel.writeString(address)
        parcel.writeStringArray(phoneNumbers.toTypedArray())
        parcel.writeString(email)
        parcel.writeString(website)
        parcel.writeString(date)
        parcel.writeStringArray(photos.toTypedArray())
        parcel.writeTypedArray(members.toTypedArray(), flags)
    }

    override fun describeContents(): Int = 0
}