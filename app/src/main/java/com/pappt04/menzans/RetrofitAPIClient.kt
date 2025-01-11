package com.pappt04.menzans

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MenzaAPIService {

    @GET("/registerUser")
    suspend fun registerUser(): Response<UserIDString>

    @POST("/enterMenza")
    suspend fun enterMenza(): Response<Void>

    @POST("/exitMenza")
    suspend fun exitMenza(): Response<Void>

    @POST("/deleteUser")
    suspend fun deleteUser(): Response<Void>

    @POST("/addmeal")
    suspend fun addMeal(@Body meal: MealEventString): Response<Void>
    
}

object RetrofitAPIClient {
}