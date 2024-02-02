package com.example.aldrovandishunt.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Stanze (
    @PrimaryKey
    val nome: String
)

