package com.example.compensation_app.Backend

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("/guards")
    fun getGuards(): Call<List<emp>>

    @POST("/aguards")
    fun addGuard(@Body emp: emp): Call<Void>

    @POST("/verify_guard")
    fun verifyGuard(@Body request: VerifyGuardRequest): Call<VerifyGuardResponse>

    @POST("/compensationform")
    fun submitCompensationForm(@Body form: CompensationForm): Call<Void>

    // ApiService.kt
    @GET("/guards/{mobile_number}")
    fun getGuardByMobileNumber(@retrofit2.http.Path("mobile_number") mobileNumber: String): Call<emp>
    @GET("/compensationform/{forest_guard_id}")
    fun getCompensationFormsByGuardId(@Path("forest_guard_id") forestGuardId: String): Call<List<RetrivalForm>>

    @GET("/compensationform/deputyranger/{dept_ranger_id}")
    fun getCompensationFormsByDeptRangerID(@Path("dept_ranger_id") deptRangerId: String): Call<List<RetrivalForm>>

    @POST("/update_form_status/{form_id}")
    fun updateFormStatus(
        @Path("form_id") formId: String,
        @Body request: UpdateFormStatusRequest
    ): Call<UpdateFormStatusResponse>


}


