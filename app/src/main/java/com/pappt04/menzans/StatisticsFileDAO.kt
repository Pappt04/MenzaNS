package com.pappt04.menzans

import android.content.Context
import java.time.format.DateTimeFormatter

class StatisticsFileDAO(context: Context, month: String) : FileDAO(context, month) {

    private val realmonth = DummyData.engmonths[month.toInt() - 1]
    private val formatter = DateTimeFormatter.ofPattern(DummyData.datetypeall.toPattern())

    fun savetoFileMonth(meal: EatingStatisticsData) {
        var s=""
        s=findMeal(meal.tokentype)

        if(s=="")
            throw Exception("Something has gone wrong, meal does not fin in defined meals")

        val s1 = "${meal.date.format(formatter)},${meal.timeentered},${meal.timeexited},$s;\n"
        context.openFileOutput(realmonth, Context.MODE_APPEND).use {
            it.write(s1.toByteArray())
        }
    }

    fun rewriteFileMonth(data: List<EatingStatisticsData>) {
        var flag=false
        data.forEach { meal ->

            var s=""
            s=findMeal(meal.tokentype)


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

    fun removeFromStatistics(
        data: List<EatingStatisticsData>,
        meal: EatingStatisticsData
    ) {
        var d: MutableList<EatingStatisticsData> = data.toMutableList()
        d.remove(meal)
        rewriteFileMonth(d)
        //monthStatisticsReWriteFile(context, (d[0].date.month.value).toString(), d)
    }

    fun findMeal(type: Uitext): String
    {
        var i = 0
        var s = ""
        for (m in DummyData.MealSample) {
            if (m.name == type) {
                 return DummyData.engmeals[i]
            }
            i++
        }
        return ""
    }

}