package com.example.aldrovandishunt.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey



@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Stanze::class,
            parentColumns = ["nome"],
            childColumns = ["stanza"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Beacon (
    @PrimaryKey
    val UID: String,
    val stanza: String
)
