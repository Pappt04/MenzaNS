package com.pappt04.menzans

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.pappt04.menzans.DummyData.CardHolderFileName
import com.pappt04.menzans.ui.theme.MenzaNSTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            0
        )

        setContent {
            MenzaNSTheme {
                val context = LocalContext.current
                val files: Array<String> = context.fileList()
                var stemp = ""
                if (CardHolderFileName in files) {
                    context.openFileInput(CardHolderFileName).bufferedReader()
                        .useLines { lines ->
                            lines.fold("") { some, text ->
                                stemp = "$some$text"
                                stemp
                            }
                        }
                } else {
                    stemp = ",,,,,,,,"
                    context.openFileOutput(CardHolderFileName, Context.MODE_PRIVATE).use {
                        it.write(stemp.toByteArray())
                    }
                }
                val splitstring: List<String> = stemp.split(",")
                MainNavigationDrawer(splitstring)

            }
        }
    }
}