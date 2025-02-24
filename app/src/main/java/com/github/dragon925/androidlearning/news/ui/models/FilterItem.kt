package com.github.dragon925.androidlearning.news.ui.models

import android.os.Parcel
import android.os.Parcelable
import com.github.dragon925.androidlearning.common.domain.Category

data class FilterItem(
    val category: Category,
    val isChecked: Boolean = false
) : Parcelable {

    companion object CREATOR : Parcelable.Creator<FilterItem> {
        override fun createFromParcel(parcel: Parcel): FilterItem = FilterItem(parcel)
        override fun newArray(size: Int): Array<FilterItem?> = arrayOfNulls(size)
    }

    constructor(parcel: Parcel) : this(
        parcel.readTypedObject(Category.CREATOR)!!,
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedObject(category, flags)
        parcel.writeByte(if (isChecked) 1 else 0)
    }

    override fun describeContents(): Int = 0
}