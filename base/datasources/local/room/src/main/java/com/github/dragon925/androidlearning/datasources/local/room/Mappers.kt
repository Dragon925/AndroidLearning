package com.github.dragon925.androidlearning.datasources.local.room

import com.github.dragon925.androidlearning.datasources.contract.CategoryContract
import com.github.dragon925.androidlearning.datasources.contract.EventContract
import com.github.dragon925.androidlearning.datasources.local.room.entities.CategoryEntity
import com.github.dragon925.androidlearning.datasources.local.room.entities.EventCrossCategory
import com.github.dragon925.androidlearning.datasources.local.room.entities.EventEntity
import com.github.dragon925.androidlearning.datasources.local.room.entities.EventPhotoEntity


internal fun EventContract.toEntities(): Triple<EventEntity, List<EventPhotoEntity>, List<EventCrossCategory>> {
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

internal fun CategoryContract.toEntity() = CategoryEntity(
    id = id,
    name = name,
    nameEn = nameEn,
    image = image
)