@file:Suppress("unused")
package com.pappt04.menzans.data.api

// Re-export from new service/ package for backward compatibility
typealias MenzaAPIService = com.pappt04.menzans.service.MenzaApiService
val RetrofitAPIClient = com.pappt04.menzans.service.RetrofitClient

// Re-export helper functions
fun registerNewUser(context: android.content.Context, onIdGenerated: (String) -> Unit) =
    com.pappt04.menzans.service.registerNewUser(context, onIdGenerated)

fun sendEnterEvent(userId: String, date: String, enteredTime: String, context: android.content.Context) =
    com.pappt04.menzans.service.sendEnterEvent(userId, date, enteredTime, context)

fun sendExitEvent(userId: String, exitTime: String, token: String, context: android.content.Context) =
    com.pappt04.menzans.service.sendExitEvent(userId, exitTime, token, context)

fun sendDeleteUser(userId: String, context: android.content.Context) =
    com.pappt04.menzans.service.sendDeleteUser(userId, context)

fun sendAddMeal(meal: com.pappt04.menzans.models.MealEventString, context: android.content.Context) =
    com.pappt04.menzans.service.sendAddMeal(meal, context)

fun sendRemoveMeal(meal: com.pappt04.menzans.models.MealEventString, context: android.content.Context) =
    com.pappt04.menzans.service.sendRemoveMeal(meal, context)

fun getWaitTime(context: android.content.Context, onGotWaitTime: (com.pappt04.menzans.models.WaitTime?) -> Unit) =
    com.pappt04.menzans.service.getWaitTime(context, onGotWaitTime)

fun getLineGraph(onGotMap: (Map<String, Double>?) -> Unit) =
    com.pappt04.menzans.service.getLineGraph(onGotMap)
