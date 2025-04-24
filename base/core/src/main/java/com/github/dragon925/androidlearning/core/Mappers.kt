package com.github.dragon925.androidlearning.core

import com.github.dragon925.androidlearning.core.api.domain.models.Category
import com.github.dragon925.androidlearning.core.api.domain.models.Event
import com.github.dragon925.androidlearning.datasources.contract.CategoryContract
import com.github.dragon925.androidlearning.datasources.contract.EventContract
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal fun CategoryContract.toDomain(useEn: Boolean = false) = Category(
    id = id,
    name = if (useEn) nameEn else name,
    url = image
)

internal fun EventContract.toDomain(): Event {
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