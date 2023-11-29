package com.example.posingottae.ui.socialmedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SocialViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Chatting Fragment"
    }
    val text: LiveData<String> = _text
}