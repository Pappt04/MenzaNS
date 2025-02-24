package com.pappt04.menzans.statistics

import android.content.Context
import com.pappt04.menzans.data.DummyData
import com.pappt04.menzans.data.DummyData.engtosresc
import com.pappt04.menzans.data.EatingStatisticsData
import com.pappt04.menzans.data.FileDAO
import com.pappt04.menzans.data.Uitext
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class StatisticsFileDAO(context: Context, month: String) : FileDAO(context, month) {

    private val realmonth= month
    //private val realmonth = DummyData.engmonths[month.toInt() - 1]
    private val formatter = DateTimeFormatter.ofPattern(DummyData.datetypedate.toPattern())

    private var mealEventData: MutableList<EatingStatisticsData> = converttoStatisticsMeals(readFromFile().split(";"))

    fun appendToStatisticsFile(meal: EatingStatisticsData) {
        var s=""
        s=findMeal(meal.tokentype)

        if(s=="")
            throw Exception("Something has gone wrong, meal does not fin in defined meals")

        val s1 = "${meal.date.format(formatter)},${meal.timeentered},${meal.timeexited},$s;\n"
        context.openFileOutput(realmonth, Context.MODE_APPEND).use {
            it.write(s1.toByteArray())
        }
    }

    fun getStatisticsData(): MutableList<EatingStatisticsData>
    {
        return mealEventData
    }

    fun saveStatisticsToFile(data: List<EatingStatisticsData> = mealEventData) {
        var flag=false
        data.forEach { meal ->

            var s=findMeal(meal.tokentype)

            if(s=="")
                throw Exception("Something has gone wrong, meal does not fin in defined meals")

            val s1 = "${meal.date.format(formatter)},${meal.timeentered},${meal.timeexited},$s;\n"
            if(flag) {
                context.openFileOutput(realmonth, Context.MODE_APPEND).use {
                    it.write(s1.toByteArray())
                }
            } else
            {
                context.openFileOutput(realmonth, Context.MODE_PRIVATE).use {
                    it.write(s1.toByteArray())
                }
                flag=true
            }
        }
    }

    fun converttoStatisticsMeals(splitData: List<String>): MutableList<EatingStatisticsData> {
        lateinit var elements: List<String>

        var temp: EatingStatisticsData

        val formattedlist = mutableListOf<EatingStatisticsData>()
        for (split in splitData) {
            if (split != "") {
                elements = split.split(",")
                temp = EatingStatisticsData(
                    LocalDate.parse(elements[0],DateTimeFormatter.ofPattern(DummyData.datetypedate.toPattern())) ,
                    elements[1],
                    elements[2],
                    Uitext.StringResource(engtosresc(elements[3]))
                )
                formattedlist += (temp)
            }
        }
        return formattedlist
    }

    fun removeFromStatistics(meal: EatingStatisticsData): Boolean
    {
        return mealEventData.remove(meal)
    }

    fun addToStatistics(meal: EatingStatisticsData): MutableList<EatingStatisticsData>
    {
        mealEventData.add(meal)
        return mealEventData
    }

    private fun findMeal(type: Uitext): String
    {
        for ((i, m) in DummyData.MealSampleBudget.withIndex()) {
            if (m.name == type) {
                 return DummyData.engmeals[i]
            }
        }
        return ""
    }

}