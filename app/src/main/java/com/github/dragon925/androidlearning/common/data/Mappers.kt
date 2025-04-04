package com.github.dragon925.androidlearning.common.data

import com.github.dragon925.androidlearning.common.data.models.CategoryDto
import com.github.dragon925.androidlearning.common.data.models.EventDto
import com.github.dragon925.androidlearning.common.domain.Category
import com.github.dragon925.androidlearning.common.domain.Event
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun CategoryDto.toDomain(useEn: Boolean = false) = Category(
    id = id,
    name = if (useEn) nameEn else name,
    url = image
)

fun EventDto.toDomain(): Event {
    val currentTimeZone = TimeZone.currentSystemDefault()

    return Event(
        id = id,
        name = name,
        description = description,
        organizer = organization,
        categoryIds = categories,
        address = address,
        phoneNumbers = phone.split("\n"),
        email = "",
        website = "",
        startDate = startDate.toLocalDate(currentTimeZone),
        endDate = endDate.toLocalDate(currentTimeZone),
        photos = photos
    )
}

private fun Long.toLocalDate(timezone: TimeZone): LocalDate {
    return Instant.fromEpochMilliseconds(this).toLocalDateTime(timezone).date
}