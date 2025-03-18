package com.pappt04.menzans.appui

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.pappt04.menzans.appui.animations.AnimatedAppearance
import com.pappt04.menzans.appui.navigationdrawer.MainNavigationDrawer
import com.pappt04.menzans.data.FileContainer
import com.pappt04.menzans.data.FileContainer.CardHolderFileName
import com.pappt04.menzans.data.FileContainer.FileUserID
import com.pappt04.menzans.data.FileDAO
import com.pappt04.menzans.data.UserIDString
import com.pappt04.menzans.data.registerNewUser
import com.pappt04.menzans.notifications.createChannel
import com.pappt04.menzans.ui.theme.MenzaNSTheme

lateinit var UserID: UserIDString

class MainActivity : AppCompatActivity() {

    private val NOTIFICATION_PERMISSION_CODE = 1004
    private val ALL_LOCATION_PERMISSIONS = 1010

    private var savedMeals: SnapshotStateList<Int> = SnapshotStateList<Int>()

    private lateinit var globalContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            //TODO REQUEST PERMISSIONS ON APP LAUNCH
            val context = LocalContext.current
            globalContext = context
        }
    }


    //TODO LOAD ALL FILES IN ON CREATE SO THERE IS KNOW MICROLAGS WHEN USER INTERACT WITH THE APP
    override fun onStart() {
        super.onStart()

        setContent {
            val scope = rememberCoroutineScope()

            val context = LocalContext.current

            val theme = remember { mutableStateOf(false) }

            val materialtheme = remember { mutableStateOf(false) }

            val onBudgetPricing = remember { mutableStateOf(false) }

            //**************************************************************//

            val dao = FileDAO(this, FileContainer.FileDarkThemeEnabled)

            val saveddark = dao.readFromFile()
            theme.value = saveddark != "" && saveddark.toInt() == 1


            //**************************************************************//
            val mdao = FileDAO(this, FileContainer.FileMaterialYouEnabled)
            val savedmaterial = mdao.readFromFile()
            materialtheme.value = savedmaterial != "" && savedmaterial.toInt() == 1


            //**************************************************************//
            val bdao = FileDAO(this, FileContainer.FileMealPricing)
            val savedpricing = bdao.readFromFile()
            onBudgetPricing.value = savedpricing != "" && savedpricing.toInt() == 1


            //**************************************************************//
            UserID = getUserID(context)

            MenzaNSTheme(darkTheme = theme.value, dynamicColor = materialtheme.value) {

                requestAllPermissions()
                createChannel(context)

                val d = calculateRemainingMeals(context)
                savedMeals.clear()
                for (m in d)
                    savedMeals.add(m)

                val firstwelcome = remember { mutableStateOf(true) }

                val files: Array<String> = context.fileList()
                if (CardHolderFileName in files) {
                    firstwelcome.value = false
                }

                    MainNavigationDrawer(
                        theme,
                        materialtheme,
                        onBudgetPricing,
                        firstwelcome,
                        savedMeals
                    )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        val context = this
        for ((i, m) in savedMeals.withIndex()) {
            val fdao = FileDAO(context, FileContainer.FileNames[i])
            fdao.saveToFile(m)
        }

        saveUserID(context, UserID.userid)

    }

    private fun saveUserID(context: Context, idstring: String) {
        if (idstring == "")
            return

        context.openFileOutput(FileUserID, Context.MODE_PRIVATE).use {
            it.write(idstring.toByteArray())
        }
    }


    private fun getUserID(context: Context): UserIDString {
        val fileDAO = FileDAO(context, FileUserID)

        val ids = UserIDString(fileDAO.getDAOData())
        if (ids.userid == "") {
            registerNewUser(context) { newId ->
                ids.userid = newId
                saveUserID(context, newId)
            }
        }
        return ids
    }

    private fun calculateRemainingMeals(context: Context): Array<Int> {
        val files: Array<String> = context.fileList()
        var remainingOnCard: Array<Int> = emptyArray()
        var s1 = ""
        for (s in FileContainer.FileNames) {
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

}