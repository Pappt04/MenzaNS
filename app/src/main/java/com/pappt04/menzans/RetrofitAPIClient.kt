package com.pappt04.menzans

import android.content.Context
import android.widget.Toast
import com.pappt04.menzans.DummyData.BASE_SERVER_URL
import com.pappt04.menzans.DummyData.FileUserID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface MenzaAPIService {

    @GET("/registerUser")
    suspend fun registerUser(): Response<UserIDString>

    @POST("/deleteUser")
    suspend fun deleteUser(@Body id: UserIDString): Response<Void>

    @POST("/enterMenza")
    suspend fun enterMenza(@Body ee: EnterEventString): Response<Void>

    @POST("/exitMenza")
    suspend fun exitMenza(@Body ee: ExitEventString): Response<Void>

    @POST("/addmeal")
    suspend fun addMeal(@Body meal: MealEventString): Response<Void>

    @PUT("/removemeal")
    suspend fun removeMeal(@Body meal: MealEventString): Response<Void>

    @GET("/waittime")
    suspend fun  getWaitTime(): Response<WaitTime>

}

object RetrofitAPIClient {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        //level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        level= HttpLoggingInterceptor.Level.BODY
        level= HttpLoggingInterceptor.Level.NONE
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_SERVER_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: MenzaAPIService by lazy {
        retrofit.create(MenzaAPIService::class.java)
    }
}

fun registerNewUser(context: Context, onIdGenerated: (String) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitAPIClient.apiService.registerUser()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val newId = response.body()?.userid ?: ""

                    onIdGenerated(newId)
                    Toast.makeText(context, "Registered ID: $newId", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to register ID: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error reaching out to server ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

fun sendEnterEvent(
    userId: String,
    date: String,
    enteredTime: String,
    context: Context
) {

    if (userId.isEmpty() || date.isEmpty() || enteredTime.isEmpty()) {
        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
        return
    }
    val eventData = EnterEventString(userId, date, enteredTime)

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitAPIClient.apiService.enterMenza(eventData)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    //Toast.makeText(context, "Event submitted successfully", Toast.LENGTH_SHORT).show()
                } else {
                    //Toast.makeText(context, "Submission failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

fun sendExitEvent(
    userId: String,
    exitTime: String,
    token: String,
    context: android.content.Context
) {

    if (userId.isEmpty() || token.isEmpty() || exitTime.isEmpty()) {
        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
        return
    }
    val eventData = ExitEventString(userId, exitTime,token)

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitAPIClient.apiService.exitMenza(eventData)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    //Toast.makeText(context, "Event submitted successfully", Toast.LENGTH_SHORT).show()
                } else {
                    //Toast.makeText(context, "Submission failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

fun sendDeleteUser(
    userId: String,
    context: Context
){
    if(userId == "")
        return

    val deletedUser= UserIDString(userId)

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitAPIClient.apiService.deleteUser(deletedUser)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    //Toast.makeText(context, "Event submitted successfully", Toast.LENGTH_SHORT).show()
                } else {
                    //Toast.makeText(context, "Submission failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

fun sendAddMeal(
    meal: MealEventString,
    context: Context
){
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitAPIClient.apiService.addMeal(meal)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    //Toast.makeText(context, "Event submitted successfully", Toast.LENGTH_SHORT).show()
                } else {
                    //Toast.makeText(context, "Submission failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

fun sendRemoveMeal(
    meal: MealEventString,
    context: Context
)
{
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitAPIClient.apiService.removeMeal(meal)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    //Toast.makeText(context, "Event submitted successfully", Toast.LENGTH_SHORT).show()
                } else {
                    //Toast.makeText(context, "Submission failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

fun getWaitTime(context: Context, onGotWaitTime: (WaitTime?) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitAPIClient.apiService.getWaitTime()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val wt = response.body()

                    onGotWaitTime(wt)
                    //Toast.makeText(context, "Registered ID: $newId", Toast.LENGTH_SHORT).show()
                } else {
                    //Toast.makeText(context, "Failed to register ID: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error reaching out to server ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}



