package com.pappt04.menzans

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.pappt04.menzans.DummyData.CardHolderFileName
import com.pappt04.menzans.DummyData.FileUserID
import com.pappt04.menzans.ui.theme.MenzaNSTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.js.ExperimentalJsFileName

lateinit var UserID: UserIDString

class MainActivity : AppCompatActivity() {

    private val NOTIFICATION_PERMISSION_CODE = 1004
    private val ALL_LOCATION_PERMISSIONS = 1010

    private lateinit var geofenceManager: GeofenceManager

    private var savedMeals: SnapshotStateList<Int> = SnapshotStateList<Int>()

    private lateinit var globalContext:Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            //TODO REQUEST PERMISSIONS ON APP LAUNCH
            val context = LocalContext.current
            globalContext=context
        }
    }


    //TODO LOAD ALL FILES IN ON CREATE SO THERE IS KNOW MICROLAGS WHEN USER INTERACT WITH THE APP
    override fun onStart() {
        super.onStart()

        setContent {
            val context= LocalContext.current

            var theme = remember { mutableStateOf(false) }

            var dao: FileDAO= FileDAO(this,DummyData.FileDarkThemeEnabled)
            val saveddark = dao.readFromFile()

            theme.value = saveddark != "" && saveddark.toInt() == 1

            UserID= getUserID(context)

            MenzaNSTheme(darkTheme = theme.value) {

                geofenceManager = GeofenceManager(context)

                for (geofence in DummyData.LANDMARK_DATA) {
                    geofenceManager.addGeofence(
                        geofence.key,
                        geofence.location,
                        geofence.radiusInMeters,
                        geofence.expirationTimeInMillis
                    )
                }
                geofenceManager.registerGeofence()

                requestAllPermissions()
                createChannel(context)

                val d= calculateRemainingMeals(context)
                savedMeals.clear()
                for(m in d )
                    savedMeals.add(m)

                MainNavigationDrawer(loadCardHolder(context), theme,savedMeals)

            }
        }
    }

    override fun onPause() {
        super.onPause()
        val context=this
        for((i, m) in savedMeals.withIndex())
        {
            val fdao= FileDAO(context,DummyData.FileNames[i])
            fdao.saveToFile(m)
        }

        saveUserID(context, UserID.userid)

    }

    private fun saveUserID(context: Context, idstring: String)
    {
        if (idstring == "")
            return

        context.openFileOutput(FileUserID, Context.MODE_PRIVATE).use {
            it.write(idstring.toByteArray())
        }
    }


    private fun getUserID(context: Context): UserIDString
    {
        val fileDAO= FileDAO(context, FileUserID)

        val ids= UserIDString(fileDAO.getDAOData())
        if (ids.userid == "")
        {
            registerNewUser(context) { newId ->
                ids.userid=newId
                saveUserID(context,newId)
            }
        }
        return ids
    }

    private fun calculateRemainingMeals(context: Context): Array<Int>
    {
        val files: Array<String> = context.fileList()
        var remainingOnCard: Array<Int> = emptyArray()
        var s1 = ""
        for (s in DummyData.FileNames) {
            if (s in files) {
                context.openFileInput(s).bufferedReader().useLines { lines ->
                    lines.fold("") { some, text ->
                        s1 = "$some$text"
                        s1
                    }
                }
            } else {
                s1 = "0"
                context.openFileOutput(s, Context.MODE_PRIVATE).use {
                    it.write(s1.toByteArray())
                }
            }
            remainingOnCard += s1.toInt()
        }
        return remainingOnCard
    }

    private fun loadCardHolder(context: Context): List<String>
    {
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
        return stemp.split(",")
    }

    private fun requestAllPermissions() {
        requestAllLocationPermission()
        requestNotificationLocationPermission()
    }

    private fun requestAllLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ),
            ALL_LOCATION_PERMISSIONS
        )
    }

    private fun requestNotificationLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ALL_LOCATION_PERMISSIONS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(this, "Haha W", Toast.LENGTH_SHORT).show()
                    for (geofence in DummyData.LANDMARK_DATA) {
                        geofenceManager.addGeofence(
                            geofence.key,
                            geofence.location,
                            geofence.radiusInMeters,
                            geofence.expirationTimeInMillis
                        )
                    }
                    geofenceManager.registerGeofence()
                } else {
                    //TODO NOTHING?
                }
            }

            NOTIFICATION_PERMISSION_CODE -> {
                //TODO
            }

        }
    }
}