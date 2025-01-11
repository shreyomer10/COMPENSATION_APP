package com.example.compensation_app.Backend

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("/guards")
    fun getGuards(): Call<List<Guard>>

    @POST("/aguards")
    fun addGuard(@Body guard: Guard): Call<Void>

    @POST("/verify_guard")
    fun verifyGuard(@Body request: VerifyGuardRequest): Call<VerifyGuardResponse>
}


