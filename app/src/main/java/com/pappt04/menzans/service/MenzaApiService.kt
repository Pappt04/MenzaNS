package com.pappt04.menzans.service

import com.pappt04.menzans.data.consts.AppConfig.BASE_API_NAME
import com.pappt04.menzans.models.DayMenu
import com.pappt04.menzans.models.EnterEventString
import com.pappt04.menzans.models.ExitEventString
import com.pappt04.menzans.models.MealEventString
import com.pappt04.menzans.models.UserIDString
import com.pappt04.menzans.models.WaitTime
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT

interface MenzaApiService {
    @Headers("Accept: application/json")
    @GET("$BASE_API_NAME/registerUser")
    suspend fun registerUser(): Response<UserIDString>

    @Headers("Accept: application/json")
    @POST("$BASE_API_NAME/deleteUser")
    suspend fun deleteUser(
        @Body id: UserIDString,
    ): Response<Void>

    @Headers("Accept: application/json")
    @POST("$BASE_API_NAME/enterMenza")
    suspend fun enterMenza(
        @Body ee: EnterEventString,
    ): Response<Void>

    @Headers("Accept: application/json")
    @POST("$BASE_API_NAME/exitMenza")
    suspend fun exitMenza(
        @Body ee: ExitEventString,
    ): Response<Void>

    @Headers("Content-Type: application/json")
    @POST("$BASE_API_NAME/addmeal")
    suspend fun addMeal(
        @Body meal: MealEventString,
    ): Response<Void>

    @Headers("Accept: application/json")
    @PUT("$BASE_API_NAME/removemeal")
    suspend fun removeMeal(
        @Body meal: MealEventString,
    ): Response<Void>

    @Headers("Accept: application/json")
    @GET("$BASE_API_NAME/waittime")
    suspend fun getWaitTime(): Response<WaitTime>

    @Headers("Accept: application/json")
    @GET("$BASE_API_NAME/lineGraph")
    suspend fun getLineGraph(): Response<Map<String, Double>>

    @Headers("Accept: application/json")
    @GET("appapi/menu/today")
    suspend fun getTodayMenu(): Response<DayMenu>
}
