package com.example.aldrovandishunt.data.database

import kotlinx.coroutines.flow.Flow

class HuntRepository (private val huntDao: DatabaseDao){
    suspend fun unlockCard(cardId: Int) = huntDao.unlockCard(cardId)
    fun getCards(room: String) : Flow<List<Carte>> = huntDao.getCards(room)
    fun getRoom(uid: String) : String = huntDao.getRoom(uid)
    fun getUnlockedCards() : Flow<List<Carte>> = huntDao.getUnlockedCards()
}