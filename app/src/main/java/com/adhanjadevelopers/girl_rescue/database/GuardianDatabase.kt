package com.adhanjadevelopers.girl_rescue.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AddGuardian::class], version = 5, exportSchema = false)
abstract class GuardianDatabase : RoomDatabase() {
    //gives access to dao methods
    abstract val guardianDao: GuardianDao

    companion object {
        //make changes visible to other threads
        @Volatile
        private var INSTANCE: GuardianDatabase? = null

        fun getInstance(context: Context): GuardianDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        GuardianDatabase::class.java,
                        "guardians_database"
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}