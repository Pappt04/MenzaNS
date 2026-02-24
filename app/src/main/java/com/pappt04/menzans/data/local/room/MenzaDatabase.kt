package com.pappt04.menzans.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [MealEventEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MenzaDatabase : RoomDatabase() {
    abstract fun mealEventDao(): MealEventDao

    companion object {
        fun create(context: Context): MenzaDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                MenzaDatabase::class.java,
                "menza_database"
            ).build()
        }
    }
}
