package com.example.aldrovandishunt.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Beacon::class, Carte::class, Stanze::class], version = 1, exportSchema = false)
abstract class HuntDatabase : RoomDatabase(){
    abstract fun DatabaseDao(): DatabaseDao

    companion object{
        @Volatile
        private var Instance: HuntDatabase? = null
        fun getDatabase(context: Context): HuntDatabase{
            return Instance ?: synchronized(this){
                Room.databaseBuilder(context, HuntDatabase::class.java, "hunt_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}