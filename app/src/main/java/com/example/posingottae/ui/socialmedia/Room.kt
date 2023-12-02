package com.example.posingottae.ui.socialmedia

data class Room(
    val roomId: String = "",
    val roomName: String = "",
    val members: List<String> = emptyList()
)
