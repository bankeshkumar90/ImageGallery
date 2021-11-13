package com.nowfloats.packrat.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nowfloats.packrat.roomdatabase.modal.ProductPropertiesConverter
import com.nowfloats.packrat.roomdatabase.modal.ProductProperty

//database class which returns the instance of our database
@Database(entities = [EntityClass::class,ProductFormData::class, productDataInfo::class], version = 1)
@TypeConverters(ProductTypeConverter::class)
abstract class DatabaseClass : RoomDatabase() {

    abstract fun getImageDao(): DaoClass

    companion object {
        private var INSTANCE: DatabaseClass? = null

        fun getDatabase(context: Context): DatabaseClass {
            if (INSTANCE == null) {
                val builder = Room.databaseBuilder(
                    context,
                    DatabaseClass::class.java,
                    "smartcat_db"
                )
                INSTANCE = builder.build()
                return INSTANCE!!
            }
            return INSTANCE!!
        }
    }
}