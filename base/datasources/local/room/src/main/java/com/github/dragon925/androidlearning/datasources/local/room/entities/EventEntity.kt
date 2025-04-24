package com.github.dragon925.androidlearning.datasources.local.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = EventEntity.TABLE_NAME)
internal data class EventEntity(
    @PrimaryKey
    @ColumnInfo(name = ID)
    val id: String,

    val name: String,
    @ColumnInfo("start_date") val startDate: Long,
    @ColumnInfo("end_date") val endDate: Long,
    val description: String,
    val status: Long,
    val createAt: Long,
    val phone: String,
    val address: String,
    val organization: String,
    val read: Boolean = false
) {
    companion object {
        const val TABLE_NAME = "events"
        const val ID = "id"
    }
}