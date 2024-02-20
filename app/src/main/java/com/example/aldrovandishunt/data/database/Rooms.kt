package com.example.aldrovandishunt.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Rooms (
    @PrimaryKey
    val id: Int,
    val name: String,
    val description: String
)

