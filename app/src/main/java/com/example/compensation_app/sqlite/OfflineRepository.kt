package com.example.compensation_app.sqlite

import com.example.compensation_app.Backend.FormData
import com.example.compensation_app.Backend.emp
import javax.inject.Inject


class OfflineRepsitory @Inject constructor(
    private val FormDao:DraftFormDao,
    private val GuardDao :GuardDao):Repository {
    override suspend fun getAllForms() = FormDao.getAllForms()
    override suspend fun insertEntry(formData: FormData) = FormDao.insertEntry(formData)
    override suspend fun deleteAll() = FormDao.deleteAllEntries()
    override suspend fun delete(id: Int) = FormDao.deleteEntry(id = id)
    override suspend fun update(formData: FormData) = FormDao.updateFormData(formData = formData)
    override suspend fun insertGuard(emp: emp)=GuardDao.insertGuardDetails(emp)
    override suspend fun getGuard() =GuardDao.getGuard()
    override suspend fun deleteEmp(emp: emp) =GuardDao.delete(emp)
}