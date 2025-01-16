package com.example.compensation_app.Backend

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("/guards")
    fun getGuards(): Call<List<Guard>>

    @POST("/aguards")
    fun addGuard(@Body guard: Guard): Call<Void>

    @POST("/verify_guard")
    fun verifyGuard(@Body request: VerifyGuardRequest): Call<VerifyGuardResponse>

    @POST("/compensationform")
    fun submitCompensationForm(@Body form: CompensationForm): Call<Void>

    // ApiService.kt
    @GET("/guards/{mobile_number}")
    fun getGuardByMobileNumber(@retrofit2.http.Path("mobile_number") mobileNumber: String): Call<Guard>
    @GET("/compensationform/{forest_guard_id}")
    fun getCompensationFormsByGuardId(@Path("forest_guard_id") forestGuardId: String): Call<List<RetrivalForm>>


}


