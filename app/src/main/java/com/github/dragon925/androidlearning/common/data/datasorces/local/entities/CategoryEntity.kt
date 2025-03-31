package com.github.dragon925.androidlearning.common.data.datasorces.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CategoryEntity.TABLE_NAME)
data class CategoryEntity(
    @PrimaryKey
    @ColumnInfo(name = ID)
    val id: String,

    val name: String,
    @ColumnInfo("name_en") val nameEn: String = "",
    val image: String = ""
) {
    companion object {
        const val TABLE_NAME = "categories"
        const val ID = "id"
    }
}