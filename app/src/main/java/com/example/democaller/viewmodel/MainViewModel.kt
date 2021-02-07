package com.example.democaller.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.democaller.repository.BlockLogicRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val updateNumber: MutableLiveData<String> = MutableLiveData()

    private val context = application


    fun setNumber(number: String) {
        val newNumber = number
        if (updateNumber.value == newNumber) {
            return
        }
        updateNumber.value = number

        BlockLogicRepository().updateTheBlockedNumber(updateNumber.value!!, context)


    }


}