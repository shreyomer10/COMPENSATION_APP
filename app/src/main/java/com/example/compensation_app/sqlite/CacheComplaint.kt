package com.example.compensation_app.sqlite

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.compensation_app.Backend.FormData
import com.example.compensation_app.Backend.RetrivalFormShort
import com.example.compensation_app.Backend.UserComplaintRetrievalFormShort
import com.example.compensation_app.Backend.emp
@Dao
interface CacheComplaint{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun  insertComplaint(form: UserComplaintRetrievalFormShort)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBulkComplaint(forms: List<UserComplaintRetrievalFormShort>) // Bulk insert

    @Query("DELETE FROM ComplaintShortCache")
    suspend fun deleteAllComplaints()


    @Query("SELECT * FROM ComplaintShortCache")
    suspend fun getAllComplaints(): List<UserComplaintRetrievalFormShort>

}