package com.nowfloats.packrat.roomdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prod_table")
data class ProductEntityClass(
    @ColumnInfo(name = "CollectionId") var CollectionId: String,
    @ColumnInfo(name = "DataUploaded") var DataUploaded: Boolean,
    @ColumnInfo(name = "metaDataString") var metaDataString: String,
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null
}