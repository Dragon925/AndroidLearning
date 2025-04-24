package com.github.dragon925.androidlearning.datasources.local.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = EventCrossCategory.TABLE_NAME,
    primaryKeys = [EventCrossCategory.EVENT_ID, EventCrossCategory.CATEGORY_ID],
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = [EventEntity.ID],
            childColumns = [EventCrossCategory.EVENT_ID],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = [CategoryEntity.ID],
            childColumns = [EventCrossCategory.CATEGORY_ID],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
internal data class EventCrossCategory(
    @ColumnInfo(name = EVENT_ID) val eventId: String,
    @ColumnInfo(name = CATEGORY_ID) val categoryId: String
) {
    companion object {
        const val TABLE_NAME = "event_cross_category"
        const val EVENT_ID = "event_id"
        const val CATEGORY_ID = "category_id"
    }
}
