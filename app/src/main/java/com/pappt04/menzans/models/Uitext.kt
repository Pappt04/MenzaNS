package com.pappt04.menzans.models

import android.content.Context
import androidx.annotation.StringRes

sealed class Uitext {
    data class DynamicString(
        val value: String
    ) : Uitext()

    data class StringResource(
        @StringRes val id: Int
    ) : Uitext()

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(id)
        }
    }
}
