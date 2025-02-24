package com.github.dragon925.androidlearning.common.domain

import android.os.Parcel
import android.os.Parcelable

data class Category(
    val id: Int,
    val name: String,
    val url: String
) : Parcelable {

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category = Category(parcel)
        override fun newArray(size: Int): Array<Category?> = arrayOfNulls(size)
    }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(url)
    }

    override fun describeContents(): Int = 0
}
