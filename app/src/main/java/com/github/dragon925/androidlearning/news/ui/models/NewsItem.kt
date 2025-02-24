package com.github.dragon925.androidlearning.news.ui.models

import android.os.Parcel
import android.os.Parcelable

data class NewsItem(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val categoryIds: Set<Int> = emptySet(),
    val image: String? = null
) : Parcelable {

    companion object CREATOR : Parcelable.Creator<NewsItem> {
        override fun createFromParcel(parcel: Parcel): NewsItem = NewsItem(parcel)
        override fun newArray(size: Int): Array<NewsItem?> = arrayOfNulls(size)
    }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createIntArray()?.toSet() ?: emptySet<Int>(),
        parcel.readString() ?: "",
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(date)
        parcel.writeIntArray(categoryIds.toIntArray())
        parcel.writeString(image)
    }

    override fun describeContents(): Int = 0
}
