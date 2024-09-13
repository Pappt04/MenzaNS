package com.pappt04.menzans

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pappt04.menzans.ui.theme.MenzaNSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //TODO JSON DATA READING NOT WORKING NEED TO FIX
        var files: Array<String> = this.fileList()
        var remainingOnCard: Array<Int> = emptyArray()
        var s1:String=""
        for (s in DummyData.FileNames) {
            if (s in files) {
                this.openFileInput(s).bufferedReader().useLines { lines ->
                    lines.fold("") { some, text ->
                        s1="$some$text"
                        s1
                    }
                }
            } else
            {
                s1= "0"
                this.openFileOutput(s, Context.MODE_PRIVATE).use {
                    it.write(s1.toByteArray())
                }
            }
            remainingOnCard+=s1.toInt()
        }


        //Log.d(TAG, "onCreate: $s1")
        val jsonMeals: List<MealData> = DummyData.MealSample
        setContent {
            MenzaNSTheme {
                ScaffoldDesign(jsonMeals, remainingOnCard)
            }
        }
    }
}