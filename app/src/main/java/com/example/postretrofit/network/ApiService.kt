package com.example.postretrofit.network

import com.example.postretrofit.model.Product
import com.example.postretrofit.model.ResponseProduct
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

//Interface containing API
interface ApiService{

    @POST("create")
    suspend fun  createProduct(@Body product:Product):ResponseProduct
}
private var BASE_URL="http://192.168.1.18:8080/"

private val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create()) // For JSON parsing
    .baseUrl(BASE_URL)
    .build()

object ProductApi {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }}