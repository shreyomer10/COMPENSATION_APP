package com.example.compensation_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compensation_app.Backend.CompensationForm
import com.example.compensation_app.Backend.Guard
import com.example.compensation_app.Backend.GuardRepository
import com.example.compensation_app.Backend.RetrivalForm
import com.example.compensation_app.Backend.VerifyGuardRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GuardViewModel @Inject constructor(
    private val guardRepository: GuardRepository // Inject the repository
) : ViewModel() {

    // Function to fetch guards from the repository
    fun getGuards(onResult: (List<Guard>?, String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            guardRepository.getGuards(onResult)
        }
    }

    // Function to add a guard
    fun addGuard(guard: Guard, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            guardRepository.addGuard(guard, onResult)
        }
    }

    // Function to verify a guard
    fun verifyGuard(empId: String, mobileNumber: String, onResult: (String, Guard?) -> Unit) {
        val request = VerifyGuardRequest(emp_id = empId, mobile_number = mobileNumber)
        viewModelScope.launch(Dispatchers.IO) {
            guardRepository.verifyGuard(request, onResult)
        }
    }
    fun newApplicationForm(form:CompensationForm,onResult: (Boolean, String?) -> Unit){



        viewModelScope.launch (Dispatchers.IO){
            guardRepository.submitCompensationForm(form,onResult)
        }
    }
    fun searchByMobile(mobile:String,onResult: (Guard?, String?) -> Unit){
        viewModelScope.launch (Dispatchers.IO){
            guardRepository.getGuardByMobileNumber(mobileNumber = mobile,onResult)
        }
    }
    fun getFormsByID(GuardId: String, onResult: (List<RetrivalForm>?, String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            guardRepository.getCompensationFormsByGuardId(GuardId) { forms, message ->
                // Switching to Main thread to update UI
                onResult(forms,message)
            }
        }
    }


}
