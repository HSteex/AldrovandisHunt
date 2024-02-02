package com.example.aldrovandishunt.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DatabaseDao {
    @Query("UPDATE Carte SET isUnlocked=1 WHERE ID=:id")
    suspend fun unlockCard(id: Int)

    @Query("SELECT * FROM Carte WHERE stanza=:stanza")
    fun getCards(stanza: String): Flow<List<Carte>>

    @Query("SELECT Stanze.nome FROM Stanze,Beacon WHERE UID=:uid")
    fun getRoom(uid: String): String

    @Query("SELECT * FROM Carte WHERE isUnlocked=1")
    fun getUnlockedCards(): Flow<List<Carte>>


}