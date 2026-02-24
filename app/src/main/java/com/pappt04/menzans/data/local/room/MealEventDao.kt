package com.pappt04.menzans.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.time.LocalDate

@Dao
interface MealEventDao {
    @Insert
    suspend fun insert(event: MealEventEntity)

    @Query("DELETE FROM meal_events WHERE date = :date AND timeEntered = :timeEntered AND timeExited = :timeExited AND mealType = :mealType")
    suspend fun deleteByFields(date: LocalDate, timeEntered: String, timeExited: String, mealType: String)

    @Query("SELECT * FROM meal_events WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    suspend fun getEventsForDateRange(startDate: LocalDate, endDate: LocalDate): List<MealEventEntity>
}
