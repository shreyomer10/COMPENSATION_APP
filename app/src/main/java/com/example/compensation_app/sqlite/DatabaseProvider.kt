package com.example.minor_project.Sqlite

import android.content.Context
import androidx.room.Room
import com.example.compensation_app.sqlite.AppDatabase

object DatabaseProvider {
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        if (INSTANCE == null) {
            synchronized(AppDatabase::class) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "draft_db"
                ).fallbackToDestructiveMigration().build()

            }
        }
        return INSTANCE!!
    }
}