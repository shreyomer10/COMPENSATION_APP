package com.example.compensation_app.sqlite

import com.example.compensation_app.Backend.FormData
import com.example.compensation_app.Backend.RetrivalFormShort
import com.example.compensation_app.Backend.UserComplaintRetrievalFormShort
import com.example.compensation_app.Backend.emp
import javax.inject.Inject


class OfflineRepsitory @Inject constructor(
    private val FormDao:DraftFormDao,
    private val CompensationShortCache:CacheCompForm,
    private val ComplaintShortCache:CacheComplaint,
    private val GuardDao :GuardDao):Repository{
    override suspend fun getAllForms() = FormDao.getAllForms()
    override suspend fun insertEntry(formData: FormData) = FormDao.insertEntry(formData)
    override suspend fun deleteAll() = FormDao.deleteAllEntries()
    override suspend fun delete(id: Int) = FormDao.deleteEntry(id = id)
    override suspend fun update(formData: FormData) = FormDao.updateFormData(formData = formData)
    override suspend fun insertGuard(emp: emp)=GuardDao.insertGuardDetails(emp)
    override suspend fun getGuard() =GuardDao.getGuard()
    override suspend fun deleteEmp(emp: emp) =GuardDao.delete(emp)
    override suspend fun getAllCompensationShortCache(): List<RetrivalFormShort> =CompensationShortCache.getAllForms()
    override suspend fun addCompensationShortCache(forms: List<RetrivalFormShort>)= CompensationShortCache.insertBulkCompForms(forms=forms)
    override suspend fun deleteCompensationShortCache()=CompensationShortCache.deleteAllCompForms()

    override suspend fun addComplaintShortCache(forms: List<UserComplaintRetrievalFormShort>)=ComplaintShortCache.insertBulkComplaint(forms = forms)
    override suspend fun getAllComplaintShortCache(): List<UserComplaintRetrievalFormShort> =ComplaintShortCache.getAllComplaints()
    override suspend fun deleteComplaintShortCache()=ComplaintShortCache.deleteAllComplaints()


}