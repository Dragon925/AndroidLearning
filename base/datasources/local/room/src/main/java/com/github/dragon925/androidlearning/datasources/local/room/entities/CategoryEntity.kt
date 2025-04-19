package com.github.dragon925.androidlearning.datasources.local.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.dragon925.androidlearning.datasources.contract.CategoryContract

@Entity(tableName = CategoryEntity.TABLE_NAME)
internal data class CategoryEntity(
    @PrimaryKey
    @ColumnInfo(name = ID)
    override val id: String,

    override val name: String,

    @ColumnInfo("name_en")
    override val nameEn: String = "",

    override val image: String = ""
) : CategoryContract {
    companion object {
        const val TABLE_NAME = "categories"
        const val ID = "id"
    }
}