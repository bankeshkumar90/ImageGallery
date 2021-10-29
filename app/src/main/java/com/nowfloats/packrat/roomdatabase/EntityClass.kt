package com.nowfloats.packrat.roomdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//Entity which represents the database in form of table
@Entity(tableName = "image_table")
data class EntityClass(
    @ColumnInfo(name = "image_name") var name: String,
    @ColumnInfo(name = "time_stamp") var time: String,
//    @ColumnInfo(name = "album_name") var album: String,
    @ColumnInfo(name = "path") var path: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null
}