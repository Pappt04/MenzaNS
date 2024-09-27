package com.pappt04.menzans

import android.content.Context
import androidx.annotation.StringRes

data class MealData(
    var name: Uitext,
    var price: Int,
    var start_hour: Int,
    var start_minute: Int,
    var end_hour: Int,
    var end_minute: Int
    )

sealed class Uitext(){
    data class DynamicString(
        val value: String
    ): Uitext()
    data class StringResource(
        @StringRes val id: Int
    ): Uitext()

    fun asString(context: Context): String
    {
        return when (this){
            is DynamicString -> value
            is StringResource -> context.getString(id)
        }
    }
}