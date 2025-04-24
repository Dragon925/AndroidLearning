package com.github.dragon925.androidlearning.common.data.datasorces.local

import com.github.dragon925.androidlearning.common.contract.CategoryContract
import com.github.dragon925.androidlearning.common.contract.EventContract
import com.github.dragon925.androidlearning.common.data.datasorces.local.entities.CategoryEntity
import com.github.dragon925.androidlearning.common.data.datasorces.local.entities.EventCrossCategory
import com.github.dragon925.androidlearning.common.data.datasorces.local.entities.EventEntity
import com.github.dragon925.androidlearning.common.data.datasorces.local.entities.EventPhotoEntity

fun EventContract.toEntities(): Triple<EventEntity, List<EventPhotoEntity>, List<EventCrossCategory>> {
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

fun CategoryContract.toEntity() = CategoryEntity(
    id = id,
    name = name,
    nameEn = nameEn,
    image = image
)