package com.example.aldrovandishunt.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey



@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Rooms::class,
            parentColumns = ["id"],
            childColumns = ["roomId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Beacon (
    @PrimaryKey
    val UID: String,
    val roomId: Int
)
