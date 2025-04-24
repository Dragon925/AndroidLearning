package com.github.dragon925.androidlearning.datasources.local.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = EventPhotoEntity.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = [EventEntity.ID],
            childColumns = [EventPhotoEntity.EVENT_ID],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
internal data class EventPhotoEntity(
    @ColumnInfo(name = EVENT_ID) val eventId: String,
    @ColumnInfo(name = PHOTO) val photo: String,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    val id: Long = 0L,
) {
    companion object {
        const val TABLE_NAME = "event_photo"
        const val ID = "id"
        const val EVENT_ID = "event_id"
        const val PHOTO = "photo"
    }
}
