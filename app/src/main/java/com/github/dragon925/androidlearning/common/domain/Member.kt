package com.github.dragon925.androidlearning.common.domain

import android.os.Parcel
import android.os.Parcelable

data class Member(
    val id: Int,
    val name: String,
    val avatar: String,
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(avatar)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Member> {
        override fun createFromParcel(parcel: Parcel): Member = Member(parcel)
        override fun newArray(size: Int): Array<Member?> = arrayOfNulls(size)
    }
}
