package com.example.compensation_app.sqlite

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.compensation_app.Backend.emp

@Dao
interface GuardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun  insertGuardDetails(emp: emp)

    @Query("SELECT * FROM emp")
    suspend fun getGuard(): emp

    @Delete
    suspend fun delete(emp: emp)


}