package com.example.democaller.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.democaller.database.SampleDatabase
import com.example.democaller.repository.BlockLogicRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val updateNumber: MutableLiveData<String> = MutableLiveData()
    private var context = application


    fun setNumber(number: String) {
        val newNumber = number
        if (updateNumber.value == newNumber) {
            return
        }
        updateNumber.value = number
        var detailDatabase: SampleDatabase = SampleDatabase.getInstance(context)!!

        viewModelScope.launch {
            BlockLogicRepository().updateTheBlockedNumber(updateNumber.value!!, detailDatabase)

        }

    }


}