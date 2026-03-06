package com.pappt04.menzans.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.time.LocalDate

@Dao
interface MealEventDao {
    @Insert
    suspend fun insert(event: MealEventEntity)

    @Query(
        "DELETE FROM meal_events WHERE date = :date AND timeEntered = :timeEntered AND timeExited = :timeExited AND mealType = :mealType",
    )
    suspend fun deleteByFields(
        date: LocalDate,
        timeEntered: String,
        timeExited: String,
        mealType: String,
    )

    @Query("SELECT * FROM meal_events WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC LIMIT :limit OFFSET :offset")
    suspend fun getEventsForDateRange(
        startDate: LocalDate,
        endDate: LocalDate,
        limit: Int = 200,
        offset: Int = 0,
    ): List<MealEventEntity>

    @Query("SELECT * FROM meal_events ORDER BY date DESC LIMIT :limit OFFSET :offset")
    suspend fun getRecentEvents(limit: Int, offset: Int): List<MealEventEntity>

    @Query("SELECT COUNT(*) FROM meal_events")
    suspend fun getTotalEventCount(): Int
}
