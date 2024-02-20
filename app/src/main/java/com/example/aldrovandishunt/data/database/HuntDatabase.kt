package com.example.aldrovandishunt.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Beacon::class, Card::class, Rooms::class, CaptureHint::class], version = 1, exportSchema = true)
abstract class HuntDatabase : RoomDatabase(){
    abstract fun DatabaseDao(): DatabaseDao

    companion object{
        @Volatile
        private var Instance: HuntDatabase? = null
        fun getDatabase(context: Context): HuntDatabase{
            return Instance ?: synchronized(this){
                Room.databaseBuilder(context, HuntDatabase::class.java, "hunt_database")
                    .createFromAsset("database/aldrovandisHuntNEW.db")
                    .build()
                    .also { Instance = it }
            }
        }

    }
}