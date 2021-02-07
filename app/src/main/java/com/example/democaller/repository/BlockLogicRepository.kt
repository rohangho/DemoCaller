package com.example.democaller.repository

import android.content.Context
import android.widget.Toast
import com.example.democaller.database.BlockDbModel
import com.example.democaller.database.SampleDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BlockLogicRepository {

    val uiScope = CoroutineScope(Dispatchers.Main)

    fun updateTheBlockedNumber(number: String, context: Context) {

        uiScope.launch {
            getDatabase(number, context)
        }

    }

    suspend fun getDatabase(number: String, context: Context) {
        val blockDbModel = BlockDbModel()
        blockDbModel.number = number
        val detailDatabase = SampleDatabase.getInstance(context)


        detailDatabase!!.detailDAO()!!.insert(blockDbModel)

        Toast.makeText(context, "Susscessfully Added to Block", Toast.LENGTH_SHORT).show()


    }

}
