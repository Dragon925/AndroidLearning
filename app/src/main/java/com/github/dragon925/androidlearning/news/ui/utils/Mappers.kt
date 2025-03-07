package com.github.dragon925.androidlearning.news.ui.utils

import android.content.Context
import com.github.dragon925.androidlearning.R
import com.github.dragon925.androidlearning.common.domain.Event
import com.github.dragon925.androidlearning.news.ui.models.NewsDetailItem
import com.github.dragon925.androidlearning.news.ui.models.NewsItem
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

private val shortDateFormat = LocalDate.Format {
    dayOfMonth(); char('.'); monthNumber()
}

private var fullDateFormat: DateTimeFormat<LocalDate>? = null

fun Event.toNewsItem(context: Context) = NewsItem(
    id = id,
    title = name,
    description = description,
    date = getDateString(context, startDate, endDate),
    categoryIds = categoryIds.toSet(),
    image = photos.first()
)

fun Event.toNewsDetailItem(context: Context) = NewsDetailItem(
    id = id,
    name = name,
    description = description,
    organizer = organizer,
    address = address,
    phoneNumbers = phoneNumbers,
    email = email,
    website = website,
    date = getDateString(context, startDate, endDate),
    photos = photos,
    members = members
)

fun getDateString(context: Context, dayStart: LocalDate, dayEnd: LocalDate): String {
    val currentDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
    val daysUntil = currentDate.daysUntil(dayStart)

    val fullFormat = fullDateFormat ?: LocalDate.Format {
        monthName(
            MonthNames(context.resources
                .getStringArray(R.array.months_full_names)
                .toList())
        )
        char(' ')
        dayOfMonth()
        chars(", ")
        year()
    }.also { fullDateFormat = it }

    return if (daysUntil < 32) {
        with(context.resources) {
            val leftBefore = getString(R.string.left_before)
            val days = getQuantityString(R.plurals.plurals_days, daysUntil, daysUntil)
            if (dayStart == dayEnd) {
                "$leftBefore $days (${shortDateFormat.format(dayStart)})"
            } else {
                "$leftBefore $days (${shortDateFormat.format(dayStart)} – ${shortDateFormat.format(dayEnd)})"
            }
        }
    } else if (dayStart == dayEnd) {
        fullFormat.format(dayStart)
    } else {
        "${fullFormat.format(dayStart)} – ${fullFormat.format(dayEnd)}"
    }
}