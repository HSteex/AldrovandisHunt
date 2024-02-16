package com.example.aldrovandishunt.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Card::class,
            parentColumns = ["ID"],
            childColumns = ["cardId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CaptureHint(
    @PrimaryKey(autoGenerate = true)
    val ID: Int = 0,
    val cardId: Int,
    val hint: String,
    val cost: Int=0,
    val isUnlocked: Boolean=false,
)
