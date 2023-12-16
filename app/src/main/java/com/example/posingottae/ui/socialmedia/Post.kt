package com.example.posingottae.ui.socialmedia

data class Post(
    val title: String = "",
    val content: String = "",
    val timestamp: Long = 0,
    val imageUrl: String? = null
)