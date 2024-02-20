package com.example.aldrovandishunt.data.database

import kotlinx.coroutines.flow.Flow

class HuntRepository (private val huntDao: DatabaseDao){
    suspend fun unlockCard(cardId: Int) = huntDao.unlockCard(cardId)

    suspend fun unlockHint(hintId: Int) = huntDao.unlockHint(hintId)
    fun getCards(id: Int) : Flow<List<Card>> = huntDao.getCards(id)
    fun getRoom(uid: String) : String = huntDao.getRoom(uid)
    fun getUnlockedCards() : Flow<List<Card>> = huntDao.getUnlockedCards()

    fun getHint(cardId: Int) : Flow<List<CaptureHint>> = huntDao.getHint(cardId)

    fun getCard(cardId: Int) : Flow<Card> = huntDao.getCard(cardId)

    fun getCardCount(room: Int) : Flow<Int> = huntDao.getCardCount(room)

    fun getUnlockedCardCount(room: Int) : Flow<Int> = huntDao.getUnlockedCardCount(room)

    fun getRooms() : Flow<List<Rooms>> = huntDao.getRooms()

}