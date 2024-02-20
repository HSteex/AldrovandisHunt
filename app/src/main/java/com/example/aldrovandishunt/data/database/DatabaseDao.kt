package com.example.aldrovandishunt.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DatabaseDao {
    @Query("UPDATE Card SET isUnlocked=1 WHERE ID=:id")
    suspend fun unlockCard(id: Int)

    @Query("UPDATE CaptureHint SET isUnlocked=1 WHERE ID=:id")
    suspend fun unlockHint(id: Int)

    @Query("SELECT * FROM Card WHERE room=:id")
    fun getCards(id: Int): Flow<List<Card>>

    @Query("SELECT Rooms.name FROM Rooms,Beacon WHERE UID=:uid")
    fun getRoom(uid: String): String

    @Query("SELECT * FROM Card WHERE isUnlocked=1")
    fun getUnlockedCards(): Flow<List<Card>>

    @Query("SELECT * FROM CaptureHint WHERE cardId=:id ORDER BY cost ASC")
    fun getHint(id: Int): Flow<List<CaptureHint>>

    @Query("SELECT * FROM Card WHERE ID=:id")
    fun getCard(id: Int): Flow<Card>

    @Query("SELECT COUNT(*) FROM Card WHERE room=:room")
    fun getCardCount(room: Int): Flow<Int>

    @Query("SELECT COUNT(*) FROM Card WHERE room=:room AND isUnlocked=1")
    fun getUnlockedCardCount(room: Int): Flow<Int>


    @Query("SELECT * FROM Rooms")
    fun getRooms(): Flow<List<Rooms>>


}