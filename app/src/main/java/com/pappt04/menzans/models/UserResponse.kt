package com.pappt04.menzans.models

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("userid") val userID: String = "",
)
