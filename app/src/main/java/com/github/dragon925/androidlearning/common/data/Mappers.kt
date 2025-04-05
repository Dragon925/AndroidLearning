package com.github.dragon925.androidlearning.common.data

import com.github.dragon925.androidlearning.common.data.datasorces.local.entities.CategoryEntity
import com.github.dragon925.androidlearning.common.data.datasorces.local.entities.EventCrossCategory
import com.github.dragon925.androidlearning.common.data.datasorces.local.entities.EventEntity
import com.github.dragon925.androidlearning.common.data.datasorces.local.entities.EventPhotoEntity
import com.github.dragon925.androidlearning.common.data.models.CategoryDto
import com.github.dragon925.androidlearning.common.data.models.EventData
import com.github.dragon925.androidlearning.common.data.models.EventDto
import com.github.dragon925.androidlearning.common.domain.Category
import com.github.dragon925.androidlearning.common.domain.Event
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun CategoryEntity.toDomain(useEn: Boolean = false) = Category(
    id = id,
    name = if (useEn) nameEn else name,
    url = image
)

fun CategoryDto.toEntity() = CategoryEntity(
    id = id,
    name = name,
    nameEn = nameEn,
    image = image
)

fun EventData.toDomain(): Event {
    val currentTimeZone = TimeZone.currentSystemDefault()

    return Event(
        id = event.id,
        name = event.name,
        description = event.description,
        organizer = event.organization,
        categoryIds = category,
        address = event.address,
        phoneNumbers = event.phone.split("\n"),
        email = "",
        website = "",
        startDate = event.startDate.toLocalDate(currentTimeZone),
        endDate = event.endDate.toLocalDate(currentTimeZone),
        photos = photos
    )
}

fun EventDto.toEntities(): Triple<EventEntity, List<EventPhotoEntity>, List<EventCrossCategory>> {
    return Triple(
        first = EventEntity(
            id = id,
            name = name,
            startDate = startDate,
            endDate = endDate,
            description = description,
            status = status,
            createAt = createAt,
            phone = phone,
            address = address,
            organization = organization
        ),
        second = photos.filterNot { it.isBlank() }.map { url ->
            EventPhotoEntity(eventId = id, photo = url)
        },
        third = categories.filterNot { it.isBlank() }.map { categoryId ->
            EventCrossCategory(eventId = id, categoryId = categoryId)
        }
    )
}

private fun Long.toLocalDate(timezone: TimeZone): LocalDate {
    return Instant.fromEpochMilliseconds(this).toLocalDateTime(timezone).date
}