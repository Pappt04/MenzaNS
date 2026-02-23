package com.pappt04.menzans.models

import java.time.LocalDate

data class EatingStatisticsData(
    val date: LocalDate,
    val timeentered: String,
    val timeexited: String,
    val tokentype: Uitext
)
