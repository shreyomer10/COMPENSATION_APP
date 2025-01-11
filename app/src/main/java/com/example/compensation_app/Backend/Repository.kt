package com.example.compensation_app.Backend

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class GuardRepository @Inject constructor() {

    // Fetch guards from the server
    fun getGuards(onResult: (List<Guard>?, String?) -> Unit) {
        RetrofitClient.instance.getGuards().enqueue(object : Callback<List<Guard>> {
            override fun onResponse(call: Call<List<Guard>>, response: Response<List<Guard>>) {
                if (response.isSuccessful) {
                    onResult(response.body(), null)
                } else {
                    onResult(null, "Failed to fetch guards: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Guard>>, t: Throwable) {
                onResult(null, "Error: ${t.message}")
            }
        })
    }

    // Add a guard to the server
    fun addGuard(guard: Guard, onResult: (Boolean, String?) -> Unit) {
        RetrofitClient.instance.addGuard(guard).enqueue(object : Callback<Void> {
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
    fun verifyGuard(request: VerifyGuardRequest, onResult: (String, Guard?) -> Unit) {
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
}
