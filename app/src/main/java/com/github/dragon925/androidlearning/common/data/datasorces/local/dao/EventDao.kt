package com.github.dragon925.androidlearning.common.data.datasorces.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.github.dragon925.androidlearning.common.data.datasorces.local.entities.EventCrossCategory
import com.github.dragon925.androidlearning.common.data.datasorces.local.entities.EventEntity
import com.github.dragon925.androidlearning.common.data.datasorces.local.entities.EventPhotoEntity
import com.github.dragon925.androidlearning.common.data.models.EventData
import com.github.dragon925.androidlearning.common.data.models.EventDto
import com.github.dragon925.androidlearning.common.data.toEntities
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(photos: List<EventPhotoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categorise: List<EventCrossCategory>)

    @Transaction
    suspend fun saveEvents(events: List<EventDto>) {
        clear()
        for ((event, photos, categories) in events.map { it.toEntities() }) {
            insertEvent(event)
            insertPhotos(photos)
            insertCategories(categories)
        }
    }

    @Transaction
    @Query("SELECT * FROM ${EventEntity.TABLE_NAME}")
    fun loadEvents(): Flow<List<EventData>>

    @Transaction
    @Query("SELECT * FROM ${EventEntity.TABLE_NAME} WHERE ${EventEntity.ID} = :id")
    fun loadEvent(id: String): Flow<EventData>

    @Query("SELECT ${EventEntity.ID} FROM ${EventEntity.TABLE_NAME} WHERE read = 1")
    fun loadReadEventIds(): Flow<List<String>>

    @Query("UPDATE ${EventEntity.TABLE_NAME} SET read = 1 WHERE ${EventEntity.ID} IN (:ids)")
    suspend fun readEvents(vararg ids: String)

    @Query("DELETE FROM ${EventEntity.TABLE_NAME}")
    suspend fun clear()
}