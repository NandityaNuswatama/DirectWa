package com.nandits.directwa

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    private val _number = MutableLiveData<String>()
    private val _enabled = MutableLiveData<Boolean>()
    val number: LiveData<String> get() = _number
    val enable: LiveData<Boolean> get() = _enabled
    
    fun setNumber(input: String){
        _number.value = input
        _enabled.postValue(!_number.value.isNullOrEmpty())
    }
}