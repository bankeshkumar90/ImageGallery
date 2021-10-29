package com.nowfloats.packrat.roomdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prod_table")
data class ProductEntityClass(
    @ColumnInfo(name = "product_name") var pname: String,
    @ColumnInfo(name = "price_stamp") var price: String,
    @ColumnInfo(name = "barcode_no") var barcodeno: String,
    @ColumnInfo(name = "quantity") var quantity: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null
}