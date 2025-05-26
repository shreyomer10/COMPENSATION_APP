package com.example.compensation_app.sqlite

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.compensation_app.Backend.FormData
import com.example.compensation_app.Backend.RetrivalFormShort
import com.example.compensation_app.Backend.UserComplaintRetrievalFormShort
import com.example.compensation_app.Backend.emp
import com.example.compensation_app.components.Converters


@Database(entities = [
    FormData::class,
    emp::class,
    RetrivalFormShort::class,
    UserComplaintRetrievalFormShort::class], version = 12)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun CacheCompForm():CacheCompForm
    abstract fun DraftFormDao(): DraftFormDao
    abstract fun GuardDao():GuardDao

    abstract fun CacheComplaint():CacheComplaint

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "draft_db"
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}

