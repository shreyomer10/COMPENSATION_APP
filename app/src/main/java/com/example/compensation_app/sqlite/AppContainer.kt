package com.example.compensation_app.sqlite

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideStudentDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "draft_db" // Replace with your actual database name
        ).fallbackToDestructiveMigration()// This will recreate the database every time the schema changes
            .allowMainThreadQueries().build()
    }

    @Singleton
    @Provides
    fun provideDraftDao(database: AppDatabase): DraftFormDao {
        return database.DraftFormDao()
    }
    @Singleton
    @Provides
    fun provideGuardDao(database: AppDatabase): GuardDao {
        return database.GuardDao()
    }

    @Singleton
    @Provides
    fun provideCacheCompShortDao(database: AppDatabase): CacheCompForm {
        return database.CacheCompForm()
    }

    @Singleton
    @Provides
    fun provideCacheComplaintShortDao(database: AppDatabase): CacheComplaint {
        return database.CacheComplaint()
    }


}
