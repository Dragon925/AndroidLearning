package com.github.dragon925.androidlearning.common.domain.models

import android.os.Parcelable
import kotlinx.datetime.LocalDate
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler

@Parcelize
@TypeParceler<LocalDate, LocalDateParceler>()
data class Event(
    val id: String,
    val name: String,
    val description: String,
    val organizer: String,
    val categoryIds: List<String>,
    val address: String,
    val phoneNumbers: List<String>,
    val email: String,
    val website: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val photos: List<String>,
    val members: List<Member> = emptyList()
) : Parcelable
