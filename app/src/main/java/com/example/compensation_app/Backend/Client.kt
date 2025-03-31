package com.example.compensation_app.Backend

import com.example.compensation_app.Navigation.SecureStorage
import com.example.compensation_app.ui.theme.MyApplication
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//object RetrofitClient {
//  //  private const val BASE_URL = "https://web-production-5485.up.railway.app/"  // Use your API URL
//    private const val BASE_URL = "https://web-production-e76fa.up.railway.app/"  // Use your API URL
//
//    val instance: ApiService by lazy {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        retrofit.create(ApiService::class.java)
//    }
//}

object RetrofitClient {
    private const val BASE_URL = "https://web-production-e76fa.up.railway.app/"

    private val client = OkHttpClient.Builder().addInterceptor { chain ->
        val token = SecureStorage.getToken(MyApplication.appContext) // Get token

        val requestBuilder = chain.request().newBuilder()
        if (!token.isNullOrEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer $token") // Add token if available
        }

        chain.proceed(requestBuilder.build())
    }.build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
}
