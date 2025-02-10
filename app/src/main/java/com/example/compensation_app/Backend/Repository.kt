package com.example.compensation_app.Backend

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class GuardRepository @Inject constructor() {

    // Fetch guards from the server
    fun getGuards(onResult: (List<emp>?, String?) -> Unit) {
        RetrofitClient.instance.getGuards().enqueue(object : Callback<List<emp>> {
            override fun onResponse(call: Call<List<emp>>, response: Response<List<emp>>) {
                if (response.isSuccessful) {
                    onResult(response.body(), null)
                } else {
                    onResult(null, "Failed to fetch guards: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<emp>>, t: Throwable) {
                onResult(null, "Error: ${t.message}")
            }
        })
    }

    // Add a guard to the server
    fun addGuard(emp: emp, onResult: (Boolean, String?) -> Unit) {
        RetrofitClient.instance.addGuard(emp).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, "Failed to add guard: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onResult(false, "Error: ${t.message}")
            }
        })
    }

    // Verify a guard by employee ID and mobile number
    fun verifyGuard(request: VerifyGuardRequest, onResult: (String, emp?) -> Unit) {
        RetrofitClient.instance.verifyGuard(request).enqueue(object : Callback<VerifyGuardResponse> {
            override fun onResponse(
                call: Call<VerifyGuardResponse>,
                response: Response<VerifyGuardResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.employee != null) {
                        onResult("Verified", body.employee)
                    } else {
                        onResult(body?.message ?: "Employee not found", null)
                    }
                } else {
                    onResult("Error: ${response.errorBody()?.string()}", null)
                }
            }

            override fun onFailure(call: Call<VerifyGuardResponse>, t: Throwable) {
                onResult("API call failed: ${t.message}", null)
            }
        })
    }
    fun submitCompensationForm(
        form: CompensationForm,
        onResult: (Boolean, String?) -> Unit
    ) {
        RetrofitClient.instance.submitCompensationForm(form).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, response.errorBody()?.string())
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onResult(false, t.message)
            }
        })
    }
    // GuardRepository.kt
    fun getGuardByMobileNumber(mobileNumber: String, onResult: (emp?, String?) -> Unit) {
        RetrofitClient.instance.getGuardByMobileNumber(mobileNumber).enqueue(object : Callback<emp> {
            override fun onResponse(call: Call<emp>, response: Response<emp>) {
                if (response.isSuccessful) {
                    onResult(response.body(), null)
                } else {
                    onResult(null, "Failed to fetch guard: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<emp>, t: Throwable) {
                onResult(null, "Error: ${t.message}")
            }
        })
    }
    fun getCompensationFormsByGuardId(
        forestGuardId: String,
        onResult: (List<RetrivalForm>?, String?) -> Unit
    ) {
        RetrofitClient.instance.getCompensationFormsByGuardId(forestGuardId)
            .enqueue(object : Callback<List<RetrivalForm>> {
                override fun onResponse(
                    call: Call<List<RetrivalForm>>,
                    response: Response<List<RetrivalForm>>
                ) {
                    if (response.isSuccessful) {
                        onResult(response.body(), null)
                    } else {
                        onResult(null, "Failed to fetch compensation forms: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<List<RetrivalForm>>, t: Throwable) {
                    onResult(null, "Error: ${t.message}")
                }
            })
    }
    fun getCompensationFormsByDeptRangerId(
        deptRangerId: String,
        onResult: (List<RetrivalForm>?, String?) -> Unit
    ) {
        RetrofitClient.instance.getCompensationFormsByDeptRangerID(deptRangerId)
            .enqueue(object : Callback<List<RetrivalForm>> {
                override fun onResponse(
                    call: Call<List<RetrivalForm>>,
                    response: Response<List<RetrivalForm>>
                ) {
                    if (response.isSuccessful) {
                        onResult(response.body(), null)
                    } else {
                        onResult(null, "Failed to fetch compensation forms: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<List<RetrivalForm>>, t: Throwable) {
                    onResult(null, "Error: ${t.message}")
                }
            })
    }


    fun updateFormStatus(
        formId: String,
        empId: String,
        action: String,
        comments: String?,
        callback: (Result<UpdateFormStatusResponse>) -> Unit
    ) {
        val request = UpdateFormStatusRequest(empId, action, comments)

        RetrofitClient.instance.updateFormStatus(formId, request).enqueue(object : Callback<UpdateFormStatusResponse> {
            override fun onResponse(
                call: Call<UpdateFormStatusResponse>,
                response: Response<UpdateFormStatusResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback(Result.success(it))
                    } ?: callback(Result.failure(Exception("Response body is null")))
                } else {
                    callback(Result.failure(Exception("API call failed with code ${response.code()}")))
                }
            }

            override fun onFailure(call: Call<UpdateFormStatusResponse>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

}
