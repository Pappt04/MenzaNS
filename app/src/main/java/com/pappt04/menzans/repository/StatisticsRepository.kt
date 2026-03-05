package com.pappt04.menzans.repository

import com.pappt04.menzans.data.consts.CalendarData
import com.pappt04.menzans.data.consts.CalendarData.dateFormat
import com.pappt04.menzans.data.local.room.MealEventDao
import com.pappt04.menzans.data.local.room.MealEventEntity
import com.pappt04.menzans.geolocation.findEngMeal
import com.pappt04.menzans.models.EatingStatisticsData
import com.pappt04.menzans.models.MealEventString
import com.pappt04.menzans.models.Uitext
import com.pappt04.menzans.service.MenzaApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.ZoneId
import java.util.Date

class StatisticsRepository(
    private val apiService: MenzaApiService,
    private val userRepository: UserRepository,
    private val mealEventDao: MealEventDao,
) {
    suspend fun getStatisticsForMonth(month: String): List<EatingStatisticsData> {
        val monthEnum = Month.valueOf(month.uppercase())
        val currentDate = LocalDate.now()
        val year =
            if (currentDate.monthValue >= monthEnum.value) {
                currentDate.year
            } else {
                currentDate.year - 1
            }
        val yearMonth = YearMonth.of(year, monthEnum)
        val startDate = yearMonth.atDay(1)
        val endDate = yearMonth.atEndOfMonth()

        return mealEventDao.getEventsForDateRange(startDate, endDate).map { toModel(it) }
    }

    suspend fun addMealEvent(mealData: EatingStatisticsData) {
        mealEventDao.insert(toEntity(mealData))

        try {
            val meal = buildMealEventString(mealData)
            withContext(Dispatchers.IO) {
                apiService.addMeal(userRepository.getUserId(), meal)
            }
        } catch (_: Exception) {
        }
    }

    suspend fun removeMealEvent(data: EatingStatisticsData) {
        val engMeal = findEngMeal(data.tokentype)
        mealEventDao.deleteByFields(data.date, data.timeentered, data.timeexited, engMeal)
        // Server delete requires a server-assigned meal ID (DELETE /meals/{id}).
        // The app does not currently store server IDs, so the server call is skipped.
    }

    suspend fun appendMealEvent(mealData: EatingStatisticsData) {
        mealEventDao.insert(toEntity(mealData))
    }

    private fun toEntity(data: EatingStatisticsData): MealEventEntity =
        MealEventEntity(
            date = data.date,
            timeEntered = data.timeentered,
            timeExited = data.timeexited,
            mealType = findEngMeal(data.tokentype),
        )

    private fun toModel(entity: MealEventEntity): EatingStatisticsData =
        EatingStatisticsData(
            date = entity.date,
            timeentered = entity.timeEntered,
            timeexited = entity.timeExited,
            tokentype = Uitext.StringResource(CalendarData.mealNameToRes(entity.mealType)),
        )

    private fun buildMealEventString(data: EatingStatisticsData): MealEventString =
        MealEventString(
            userid = userRepository.getUserId(),
            date =
                dateFormat.format(
                    Date.from(
                        data.date.atStartOfDay(ZoneId.systemDefault()).toInstant(),
                    ),
                ),
            entered = data.timeentered,
            exited = data.timeexited,
            token = findEngMeal(data.tokentype),
        )
}
