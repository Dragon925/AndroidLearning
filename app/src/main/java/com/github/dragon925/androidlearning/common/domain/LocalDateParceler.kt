package com.github.dragon925.androidlearning.common.domain

import android.os.Parcel
import kotlinx.datetime.LocalDate
import kotlinx.parcelize.Parceler

class LocalDateParceler : Parceler<LocalDate> {

    companion object {
        private val dateFormat = LocalDate.Format {
            year(); chars("-"); monthNumber(); chars("-"); dayOfMonth()
        }
    }

    override fun LocalDate.write(parcel: Parcel, flags: Int) {
        parcel.writeString(dateFormat.format(this))
    }

    override fun create(parcel: Parcel): LocalDate {
        return dateFormat.parse(parcel.readString() ?: "")
    }
}