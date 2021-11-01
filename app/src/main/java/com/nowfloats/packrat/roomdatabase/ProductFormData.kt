package com.nowfloats.packrat.roomdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "product_table")
data class ProductFormData(
//    @ColumnInfo(name = "id") var id: Int,
    @ColumnInfo(name = "product_name") var pname: String,
    @ColumnInfo(name = "price") var price: String,
    @ColumnInfo(name = "barcode_no") var barcodeno: String,
    @ColumnInfo(name = "quantity") var quantity: String,
    @ColumnInfo(name = "others") var others: String
//    @ColumnInfo(name = "image_name") var name: String
//    @ColumnInfo(name = "path") var path: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null
}