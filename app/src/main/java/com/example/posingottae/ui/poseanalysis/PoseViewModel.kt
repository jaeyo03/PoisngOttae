package com.example.posingottae.ui.poseanalysis

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PoseViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Choose what pose do you want to follow!"
    }
    val text: LiveData<String> = _text
}