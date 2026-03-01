package com.pappt04.menzans.service

import com.pappt04.menzans.data.consts.AppConfig.BASE_API_NAME
import com.pappt04.menzans.models.DayMenu
import com.pappt04.menzans.models.EnterEventString
import com.pappt04.menzans.models.ExitEventString
import com.pappt04.menzans.models.MealEventString
import com.pappt04.menzans.models.UserResponse
import com.pappt04.menzans.models.WaitTime
import com.pappt04.menzans.models.WaitTimeSubmission
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface MenzaApiService {
    @Headers("Accept: application/json")
    @GET("$BASE_API_NAME/newuserid")
    suspend fun registerUser(): Response<UserResponse>

    @Headers("Accept: application/json")
    @DELETE("$BASE_API_NAME/users/{id}")
    suspend fun deleteUser(
        @Path("id") id: String,
    ): Response<Void>

    @Headers("Accept: application/json")
    @POST("$BASE_API_NAME/enter")
    suspend fun enterMenza(
        @Body ee: EnterEventString,
    ): Response<Void>

    @Headers("Accept: application/json")
    @POST("$BASE_API_NAME/exit")
    suspend fun exitMenza(
        @Body ee: ExitEventString,
    ): Response<Void>

    @Headers("Content-Type: application/json")
    @POST("$BASE_API_NAME/meals")
    suspend fun addMeal(
        @Body meal: MealEventString,
    ): Response<Void>

    @Headers("Accept: application/json")
    @DELETE("$BASE_API_NAME/meals/{id}")
    suspend fun removeMeal(
        @Path("id") id: Long,
    ): Response<Void>

    @Headers("Accept: application/json")
    @GET("$BASE_API_NAME/waittime")
    suspend fun getWaitTime(): Response<WaitTime>

    @Headers("Content-Type: application/json")
    @POST("$BASE_API_NAME/waittime")
    suspend fun submitWaitTime(
        @Body submission: WaitTimeSubmission,
    ): Response<Void>

    @Headers("Accept: application/json")
    @GET("$BASE_API_NAME/linegraph")
    suspend fun getLineGraph(): Response<Map<String, Double>>

    @Headers("Accept: application/json")
    @GET("$BASE_API_NAME/linegraph/local/{day}/{meal}")
    suspend fun getLineGraphForMeal(
        @Path("day") day: Int,
        @Path("meal") meal: String,
    ): Response<Map<String, Double>>

    @Headers("Accept: application/json")
    @GET("$BASE_API_NAME/menu/today")
    suspend fun getTodayMenu(): Response<DayMenu>
}
