package com.pappt04.menzans.models

import com.google.gson.annotations.SerializedName

data class ExitEventString(
    var userid: String,
    var exitTime: String,
    @SerializedName("tokentype") var token: String,
)
