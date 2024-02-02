package com.example.aldrovandishunt.data.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.aldrovandishunt.ui.myCollection.Rarity


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
data class Carte(
    @PrimaryKey(autoGenerate = true)
    val ID: Int = 0,
    val nome: String,
    val descrizione: String,
    val rarita: Rarity,
    val stanza: String,
    val isUnlocked: Boolean = false,

    //TODO aggiungere in attesa dei contenuti
)

enum class Extra{
    //TODO aggiungere in attesa dei contenuti
}