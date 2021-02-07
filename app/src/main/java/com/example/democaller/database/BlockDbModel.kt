package com.example.democaller.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "BlockedNumber")
class BlockDbModel {
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @ColumnInfo(name = "Number")
    var number: String? = null
}