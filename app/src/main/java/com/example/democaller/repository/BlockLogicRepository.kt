package com.example.democaller.repository

import com.example.democaller.database.BlockDbModel
import com.example.democaller.database.SampleDatabase

class BlockLogicRepository {


    suspend fun updateTheBlockedNumber(number: String, detailDatabase: SampleDatabase?) {

        val blockDbModel = BlockDbModel()
        blockDbModel.number = number
        detailDatabase!!.detailDAO()!!.insert(blockDbModel)


    }

}










