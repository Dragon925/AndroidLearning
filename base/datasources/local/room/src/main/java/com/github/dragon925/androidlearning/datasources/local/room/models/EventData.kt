package com.github.dragon925.androidlearning.datasources.local.room.models

import androidx.room.Embedded
import androidx.room.Relation
import com.github.dragon925.androidlearning.datasources.contract.EventContract
import com.github.dragon925.androidlearning.datasources.local.room.entities.EventCrossCategory
import com.github.dragon925.androidlearning.datasources.local.room.entities.EventEntity
import com.github.dragon925.androidlearning.datasources.local.room.entities.EventPhotoEntity

internal data class EventData(
    @Embedded val event: EventEntity,

    @Relation(
        entity = EventCrossCategory::class,
        parentColumn = EventEntity.ID,
        entityColumn = EventCrossCategory.EVENT_ID,
        projection = [EventCrossCategory.CATEGORY_ID]
    )
    override val categories: List<String>,

    @Relation(
        entity = EventPhotoEntity::class,
        parentColumn = EventEntity.ID,
        entityColumn = EventPhotoEntity.EVENT_ID,
        projection = [EventPhotoEntity.PHOTO]
    )
    override val photos: List<String>
) : EventContract {

    override val id: String get() = event.id

    override val name: String get() = event.name

    override val startDate: Long get() = event.startDate

    override val endDate: Long get() = event.endDate

    override val description: String get() = event.description

    override val status: Long get() = event.status

    override val createAt: Long get() = event.createAt

    override val phone: String get() = event.phone

    override val address: String get() = event.address

    override val organization: String get() = event.organization

}
