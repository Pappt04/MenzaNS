package com.pappt04.menzans.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "meal_events")
data class MealEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: LocalDate,
    val timeEntered: String,
    val timeExited: String,
    val mealType: String
)
