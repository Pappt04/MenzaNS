@file:Suppress("DEPRECATION")
package com.pappt04.menzans.service

import android.content.Context
import com.pappt04.menzans.models.MealEventString
import com.pappt04.menzans.models.UserIDString
import com.pappt04.menzans.models.WaitTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Transitional API helper functions for backward compatibility.
 * These use GlobalScope as a temporary solution during MVVM migration.
 * TODO: Replace with repository-based calls in composables or ViewModels.
 */

fun registerNewUser(context: Context, onIdGenerated: (String) -> Unit) {
    @Suppress("DEPRECATION")
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val response = RetrofitClient.apiService.registerUser()
            if (response.isSuccessful && response.body() != null) {
                val userId = response.body()!!.userid
                onIdGenerated(userId)
            }
        } catch (_: Exception) {
        }
    }
}

fun sendEnterEvent(userId: String, date: String, enteredTime: String, context: Context) {
    @Suppress("DEPRECATION")
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val enterEventData = com.pappt04.menzans.models.EnterEventString(userId, date, enteredTime)
            RetrofitClient.apiService.enterMenza(enterEventData)
        } catch (_: Exception) {
        }
    }
}

fun sendExitEvent(userId: String, exitTime: String, token: String, context: Context) {
    @Suppress("DEPRECATION")
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val exitEventData = com.pappt04.menzans.models.ExitEventString(userId, exitTime, token)
            RetrofitClient.apiService.exitMenza(exitEventData)
        } catch (_: Exception) {
        }
    }
}

fun sendDeleteUser(userId: String, context: Context) {
    @Suppress("DEPRECATION")
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val deletedUser = UserIDString(userId)
            RetrofitClient.apiService.deleteUser(deletedUser)
        } catch (_: Exception) {
        }
    }
}

fun sendAddMeal(meal: MealEventString, context: Context) {
    @Suppress("DEPRECATION")
    GlobalScope.launch(Dispatchers.IO) {
        try {
            RetrofitClient.apiService.addMeal(meal)
        } catch (_: Exception) {
        }
    }
}

fun sendRemoveMeal(meal: MealEventString, context: Context) {
    @Suppress("DEPRECATION")
    GlobalScope.launch(Dispatchers.IO) {
        try {
            RetrofitClient.apiService.removeMeal(meal)
        } catch (_: Exception) {
        }
    }
}

fun getWaitTime(context: Context, onGotWaitTime: (WaitTime?) -> Unit) {
    @Suppress("DEPRECATION")
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val response = RetrofitClient.apiService.getWaitTime()
            if (response.isSuccessful && response.body() != null) {
                onGotWaitTime(response.body())
            } else {
                onGotWaitTime(null)
            }
        } catch (_: Exception) {
            onGotWaitTime(null)
        }
    }
}

fun getLineGraph(onGotMap: (Map<String, Double>?) -> Unit) {
    @Suppress("DEPRECATION")
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val response = RetrofitClient.apiService.getLineGraph()
            if (response.isSuccessful && response.body() != null) {
                onGotMap(response.body())
            } else {
                onGotMap(null)
            }
        } catch (_: Exception) {
            onGotMap(null)
        }
    }
}
