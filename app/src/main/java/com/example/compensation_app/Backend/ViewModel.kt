package com.example.compensation_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.compensation_app.Backend.ApiResponse

import com.example.compensation_app.Backend.CompensationForm
import com.example.compensation_app.Backend.emp
import com.example.compensation_app.Backend.GuardRepository
import com.example.compensation_app.Backend.LoginRequest
import com.example.compensation_app.Backend.LoginResponse
import com.example.compensation_app.Backend.PdfRequest
import com.example.compensation_app.Backend.RejectComplaintRequest
import com.example.compensation_app.Backend.RetrivalForm
import com.example.compensation_app.Backend.RetrivalFormShort
import com.example.compensation_app.Backend.UpdateFormStatusResponse
import com.example.compensation_app.Backend.UserComplaintForm
import com.example.compensation_app.Backend.UserComplaintRetrievalForm
import com.example.compensation_app.Backend.UserComplaintRetrievalFormShort
import com.example.compensation_app.Backend.VerifyGuardRequest
import com.example.compensation_app.Backend.VerifyGuardResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GuardViewModel @Inject constructor(
    private val guardRepository: GuardRepository // Inject the repository
) : ViewModel() {

    fun register(empId: String,mobileNumber: String,roll: String,password:String,onResult: (ApiResponse?, String?) -> Unit) {
        val empRequest=LoginRequest(emp_id = empId,
            mobile_number = mobileNumber,
            roll=roll,
            password = password)
        viewModelScope.launch(Dispatchers.IO) {
            guardRepository.register(empRequest, onResult)
        }
    }
    fun login(empId: String,mobileNumber: String,roll: String,password:String, onResult: (LoginResponse?, String?) -> Unit) {
        val empRequest=LoginRequest(emp_id = empId,
            mobile_number = mobileNumber,
            roll=roll,
            password = password)
        viewModelScope.launch(Dispatchers.IO) {
            guardRepository.login(empRequest, onResult)
        }
    }
    fun refreshToken( onResult: (LoginResponse?, String?,Int?) -> Unit) {

        viewModelScope.launch(Dispatchers.IO) {
            guardRepository.refreshToken(onResult)
        }
    }
    fun updatePass(empId: String,mobileNumber: String,roll: String,password:String,onResult: (ApiResponse?, String?) -> Unit) {
        val empRequest=LoginRequest(emp_id = empId,
            mobile_number = mobileNumber,
            roll=roll,
            password = password)
        viewModelScope.launch(Dispatchers.IO) {
            guardRepository.updatePass(empRequest, onResult)
        }
    }

    // Function to fetch guards from the repository
    fun getGuards(onResult: (List<emp>?, String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            guardRepository.getGuards(onResult)
        }
    }

    // Function to add a guard
    fun addGuard(emp: emp, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            guardRepository.addGuard(emp, onResult)
        }
    }
    fun verifyGuard(verifyGuardRequest: VerifyGuardRequest, onResult: (VerifyGuardResponse?, String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            guardRepository.verifyGuard(verifyGuardRequest, onResult)
        }
    }

    fun newApplicationForm(form:CompensationForm,onResult: (Boolean, String?,Int?) -> Unit){
        viewModelScope.launch (Dispatchers.IO){
            guardRepository.submitCompensationForm(form,onResult)
        }
    }
    fun searchByMobile(mobile:String,onResult: (emp?, String?) -> Unit){
        viewModelScope.launch (Dispatchers.IO){
            guardRepository.getGuardByMobileNumber(mobileNumber = mobile,onResult)
        }
    }
    fun submitComplaint(form: UserComplaintForm, callback: (Boolean, Int?, String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            guardRepository.submitComplaint(form) { success, complaintId, errorMessage ->
                callback(success, complaintId, errorMessage)
            }
        }
    }

    fun getFormsByID(GuardId: String, onResult: (List<RetrivalFormShort>?, String?,Int?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            guardRepository.getCompensationFormsByGuardId(GuardId) { forms, message ,code->
                // Switching to Main thread to update UI
                onResult(forms,message,code)
            }
        }
    }
    fun getComplaint(complaintId: String, mobileNumber: String, onResult: (Boolean, UserComplaintRetrievalForm?, String?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            guardRepository.getComplaint(complaintId, mobileNumber) { success, complaint, message ->
                onResult(success, complaint, message)
            }
        }
    }
    fun fetchGuardComplaints(guardId: String, onResult: (List<UserComplaintRetrievalFormShort>?, String?,Int?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            guardRepository.getGuardComplaints(guardId) { complaints, error ,code->
                if (error != null) {
                    onResult(null, error,code)
                } else {
                    onResult(complaints, null,code)
                }
            }
        }
    }

    fun getFormsByDeptRangerID(deptRangerId: String, onResult: (List<RetrivalFormShort>?, String?, Int?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            guardRepository.getCompensationFormsByDeptRangerId(deptRangerId) { forms, message,code ->
                // Switching to Main thread to update UI
                onResult(forms,message,code)
            }
        }
    }
    fun updateStatus(
        formId: String,
        empId: String,
        action: String,
        comments: String?,
        onResult: (Result<UpdateFormStatusResponse>,Int?) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            guardRepository.updateFormStatus(formId, empId, action, comments) { result ,code->
                onResult(result,code)  // Directly returning the result
            }
        }
    }
    fun sendEmail(
        email: String,
        message: String,
        onResult: (Result<Boolean>) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            guardRepository.sendEmail(email, message) { success, error ->
                if (success) {
                    onResult(Result.success(true))
                } else {
                    onResult(Result.failure(Exception(error)))
                }
            }
        }
    }

    fun getOrCreatePdf(
        request: PdfRequest,
        onResult: (Result<String>, Int?) -> Unit  // Return download URL or error
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            guardRepository.getOrCreatePdf(request) { response, error, code ->
                if (response?.download_url != null) {
                    onResult(Result.success(response.download_url), code)
                } else {
                    onResult(Result.failure(Exception(error ?: "Failed to generate or fetch PDF")), code)
                }
            }
        }
    }


    fun rejectComplaint(
        request: RejectComplaintRequest,
        onResult: (Result<String>,Int?) -> Unit  // Returning only message or error
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            guardRepository.rejectComplaint(request) { response, error,code ->
                if (response != null) {
                    onResult(Result.success(response.message ?: "Complaint rejected successfully"),code)
                } else {
                    onResult(Result.failure(Exception(error ?: "Unknown error")),code)
                }
            }
        }
    }

    fun getEachCompensationForm(formId: String,onResult: (RetrivalForm?, String?,Int?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            guardRepository.getEachCompensationForm(formId) { form, message ,code->
                // Switching to Main thread to update UI
                onResult(form,message,code)
            }
        }
    }
    fun getEachComplaintForm(formId: String,onResult: (UserComplaintRetrievalForm?, String?,Int?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            guardRepository.getEachComplaint(formId) { form, message ,code->
                // Switching to Main thread to update UI
                onResult(form,message,code)
            }
        }
    }




}
