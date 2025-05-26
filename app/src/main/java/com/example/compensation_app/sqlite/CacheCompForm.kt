package com.example.compensation_app.sqlite

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.compensation_app.Backend.FormData
import com.example.compensation_app.Backend.RetrivalFormShort
import com.example.compensation_app.Backend.emp
@Dao
interface CacheCompForm {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun  insertCompForm(form: RetrivalFormShort)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBulkCompForms(forms: List<RetrivalFormShort>) // Bulk insert

    @Query("DELETE FROM CompensationShortCache")
    suspend fun deleteAllCompForms()


    @Query("SELECT * FROM CompensationShortCache")
    suspend fun getAllForms(): List<RetrivalFormShort>

}