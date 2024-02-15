package com.example.aldrovandishunt.data.database

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DatabaseDao {
    @Query("UPDATE Card SET isUnlocked=1 WHERE ID=:id")
    suspend fun unlockCard(id: Int)

    @Query("SELECT * FROM Card WHERE room=:stanza")
    fun getCards(stanza: String): Flow<List<Card>>

    @Query("SELECT Rooms.name FROM Rooms,Beacon WHERE UID=:uid")
    fun getRoom(uid: String): String

    @Query("SELECT * FROM Card WHERE isUnlocked=1")
    fun getUnlockedCards(): Flow<List<Card>>

    @Query("SELECT * FROM CaptureHint WHERE cardId=:id ORDER BY cost ASC")
    fun getHint(id: Int): Flow<List<CaptureHint>>

    @Query("SELECT * FROM Card WHERE ID=:id")
    fun getCard(id: Int): Flow<Card>



}