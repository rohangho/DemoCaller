package com.example.democaller.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(databaseModel: BlockDbModel?)

    @Query("Select * from BlockedNumber")
    fun query(): List<BlockDbModel?>?
}