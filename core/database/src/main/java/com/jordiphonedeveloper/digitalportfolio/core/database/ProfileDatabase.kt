package com.jordiphonedeveloper.digitalportfolio.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ProfileEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class ProfileDatabase : RoomDatabase() {

    abstract fun profileDao(): ProfileDao

    companion object {
        private const val DATABASE_NAME = "digital_portfolio.db"

        fun create(context: Context): ProfileDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                ProfileDatabase::class.java,
                DATABASE_NAME,
            ).build()
    }
}
