package com.example.compensation_app.Backend

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class Guard(
    val emp_id: String,
    val name: String,
    val mobile_number: String,
    val division: String,
    val range_: String,
    val beat: Int
)

interface ApiService {
    @GET("/guards")
    fun getGuards(): Call<List<Guard>>

    @POST("/aguards")
    fun addGuard(@Body guard: Guard): Call<Void>
}
