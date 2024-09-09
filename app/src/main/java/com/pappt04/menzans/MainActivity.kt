package com.pappt04.menzans

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pappt04.menzans.ui.theme.MenzaNSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        //val path= application.filesDir

        //TODO JSON DATA READING NOT WORKING NEED TO FIX
        //var datafromjson= readJsonAssets(this,"mealinfo.json")
        //val mealList= parseJson(datafromjson)
        var jsonMeals: List<MealData> = DummyData.MealSample
        setContent {
            MenzaNSTheme {
                ScaffoldDesign(jsonMeals)
            }
        }
    }
}

fun readJsonAssets(context: Context, filename: String): String {
    return context.assets.open(filename).bufferedReader().use { it.readText() }
}
fun parseJson(jsonString: String)
{
    return Gson().fromJson(jsonString, object : TypeToken<List<MealData>>() {}.type)
}