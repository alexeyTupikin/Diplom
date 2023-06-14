package com.example.graduationproject.WorkoutPlanCreationModule.Fragments.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelForDay: ViewModel() {
    var _client = MutableLiveData<String>().apply {
        value = "client"
    }
    var client: LiveData<String> = _client
    var _lvl = MutableLiveData<String>().apply {
        value = "lvlClient"
    }
    var lvl: LiveData<String> = _lvl
    var _exerciseDay1 = MutableLiveData<String>().apply {
        value = ""
    }
    var exerciseDay1: LiveData<String> = _exerciseDay1
    var _exerciseDay2 = MutableLiveData<String>().apply {
        value = ""
    }
    var exerciseDay2: LiveData<String> = _exerciseDay2
    var _exerciseDay3 = MutableLiveData<String>().apply {
        value = ""
    }
    var exerciseDay3: LiveData<String> = _exerciseDay3
}