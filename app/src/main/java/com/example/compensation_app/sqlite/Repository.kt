package com.example.compensation_app.sqlite

import com.example.compensation_app.Backend.FormData
import com.example.compensation_app.Backend.emp

interface Repository {
    suspend fun getAllForms(): List<FormData>
    suspend fun insertEntry(formData: FormData)
    suspend fun deleteAll()
    suspend fun update(formData: FormData)
    suspend fun delete(id:Int)

    suspend fun insertGuard(emp: emp)
    suspend fun getGuard():emp
    suspend fun deleteEmp(emp: emp)


}