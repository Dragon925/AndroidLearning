package com.github.dragon925.androidlearning.common.data.models

import androidx.room.Embedded
import androidx.room.Relation
import com.github.dragon925.androidlearning.common.data.datasorces.local.entities.EventCrossCategory
import com.github.dragon925.androidlearning.common.data.datasorces.local.entities.EventEntity
import com.github.dragon925.androidlearning.common.data.datasorces.local.entities.EventPhotoEntity

data class EventData(
    @Embedded val event: EventEntity,

    @Relation(
        entity = EventCrossCategory::class,
        parentColumn = EventEntity.ID,
        entityColumn = EventCrossCategory.EVENT_ID,
        projection = [EventCrossCategory.CATEGORY_ID]
    )
    val category: List<String>,

    @Relation(
        entity = EventPhotoEntity::class,
        parentColumn = EventEntity.ID,
        entityColumn = EventPhotoEntity.EVENT_ID,
        projection = [EventPhotoEntity.PHOTO]
    )
    val photos: List<String>
)
