package com.example.aldrovandishunt.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


enum class Rarity {
    UNCOMMON,
    RARE,
    EPIC,
    LEGENDARY
}

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Rooms::class,
            parentColumns = ["id"],
            childColumns = ["room"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Card(
    @PrimaryKey(autoGenerate = true)
    val ID: Int = 0,
    val name: String,
    val description: String,
    val rarity: Rarity,
    val room: Int,
    val isUnlocked: Boolean = false,

    //TODO aggiungere in attesa dei contenuti
)

enum class Extra{
    //TODO aggiungere in attesa dei contenuti
}