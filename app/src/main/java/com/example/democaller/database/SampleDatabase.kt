package com.example.democaller.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BlockDbModel::class], exportSchema = false, version = 1)
abstract class SampleDatabase : RoomDatabase() {
    abstract fun detailDAO(): DatabaseDao?

    companion object {
        const val dbName = "BlockNumberDb"
        var detailDatabase: SampleDatabase? = null

        @Synchronized
        fun getInstance(context: Context): SampleDatabase? {
            if (detailDatabase == null) {
                detailDatabase = Room.databaseBuilder(
                        context.applicationContext,
                        SampleDatabase::class.java,
                        dbName
                ).fallbackToDestructiveMigration().build()
            }
            return detailDatabase
        }
    }
}