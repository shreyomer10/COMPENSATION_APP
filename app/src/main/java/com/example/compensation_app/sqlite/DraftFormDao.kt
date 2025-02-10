package com.example.compensation_app.sqlite

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.compensation_app.Backend.FormData

@Dao
interface DraftFormDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun  insertGuardDetails(guard: Guard)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: FormData)






    @Query("DELETE FROM draftapplication")
    suspend fun deleteAllEntries()

    @Query("SELECT * FROM DraftApplication")
    suspend fun getAllForms(): List<FormData>

    @Query("DELETE FROM draftapplication WHERE id = :id")
    suspend fun deleteEntry(id: Int)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFormData(formData: FormData)  // Updates an existing entry
}
