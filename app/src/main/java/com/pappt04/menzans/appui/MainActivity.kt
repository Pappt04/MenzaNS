package com.pappt04.menzans.appui

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pappt04.menzans.appui.navigationdrawer.MainNavigationDrawer
import com.pappt04.menzans.data.FileContainer
import com.pappt04.menzans.data.FileContainer.CardHolderFileName
import com.pappt04.menzans.data.FileContainer.FileUserID
import com.pappt04.menzans.data.FileDAO
import com.pappt04.menzans.data.UserIDString
import com.pappt04.menzans.data.api.registerNewUser
import com.pappt04.menzans.data.mealdatastorage.MealDataStoreManager
import com.pappt04.menzans.data.mealdatastorage.MealPreferences
import com.pappt04.menzans.data.settingsdatastorage.SettingsDataStoreManager
import com.pappt04.menzans.data.settingsdatastorage.SettingsDataStoreManager.Companion.BUDGET
import com.pappt04.menzans.data.settingsdatastorage.SettingsDataStoreManager.Companion.DARK_THEME
import com.pappt04.menzans.data.settingsdatastorage.SettingsDataStoreManager.Companion.LANGUAGE
import com.pappt04.menzans.data.settingsdatastorage.SettingsDataStoreManager.Companion.MATERIALYOU_THEME
import com.pappt04.menzans.data.settingsdatastorage.SettingsDataStoreManager.Companion.TOKEN_WARNING
import com.pappt04.menzans.data.settingsdatastorage.SettingsDataStoreManager.Companion.USERID
import com.pappt04.menzans.data.settingsdatastorage.SettingsPreferences
import com.pappt04.menzans.notifications.createChannel
import com.pappt04.menzans.ui.theme.MenzaNSTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

var UserID: UserIDString = UserIDString("")

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


    override fun onStart() {
        super.onStart()

        setContent {
            val context = LocalContext.current

            val scope = rememberCoroutineScope()


//            LaunchedEffect(key1 = Unit) {
//                settingsdatamanager.saveToDataStore(
//                    SettingsPreferences(
//                        darktheme = true,
//                        materialyoutheme = true,
//                        budget = false,
//                        userID = "",
//                    )
//                )
//            }

            val isLoaded = remember { mutableStateOf(false) }

            var settingprefs: SettingsPreferences = SettingsPreferences("", "", false, false, false, 2)

            var mealprefs= MealPreferences()

            val settingsdatamanager = remember { SettingsDataStoreManager(context) }

            val theme = remember { mutableStateOf(false) }

            val materialtheme = remember { mutableStateOf(false) }

            val onBudgetPricing = remember { mutableStateOf(false) }

            val tokenwarning = remember { mutableIntStateOf(0) }

            LaunchedEffect(key1 = Unit) {
                val mealmanager= MealDataStoreManager(context)
                mealprefs=mealmanager.getFromDataStore().first()
                savedMeals.clear()

                savedMeals.add(mealprefs.breakfast)
                savedMeals.add(mealprefs.lunch)
                savedMeals.add(mealprefs.dinner)
                savedMeals.add(mealprefs.balance)


                settingprefs = settingsdatamanager.getFromDataStore().first()

                theme.value = settingprefs.darktheme
                materialtheme.value = settingprefs.materialyoutheme
                onBudgetPricing.value = settingprefs.budget
                tokenwarning.intValue = settingprefs.tokenwarning

                if (settingprefs.userID == "") {
                    val fileDAO = FileDAO(context, FileUserID)

                    settingprefs.userID = fileDAO.getDAOData()
                    if (settingprefs.userID != "") {
                        settingsdatamanager.saveToDataStore(settingprefs)
                    } else {

                        settingprefs.userID = getUserID(context).userid
                        if (settingprefs.userID != "")
                            settingsdatamanager.saveToDataStore(settingprefs)
                    }
                }

                UserID.userid=settingprefs.userID

                isLoaded.value=true
            }



            MenzaNSTheme(darkTheme = theme.value, dynamicColor = materialtheme.value) {

                requestAllPermissions()
                createChannel(context)

                val firstwelcome = remember { mutableStateOf(true) }

                val files: Array<String> = context.fileList()
                if (CardHolderFileName in files) {
                    firstwelcome.value = false
                }

                if (isLoaded.value) {
                    MainNavigationDrawer(
                        theme,
                        materialtheme,
                        onBudgetPricing,
                        firstwelcome,
                        settingsdatamanager,
                        savedMeals
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        CircularProgressIndicator()
                    }

                }

            }
        }
    }

    override fun onPause() {
        super.onPause()
        val context = this
//
//        val scope= rememberCoroutineScope()
//
//        for ((i, m) in savedMeals.withIndex()) {
//            val fdao = FileDAO(context, FileContainer.FileNames[i])
//            fdao.saveToFile(m)
//        }

//        setContent {
//            LaunchedEffect(key1 = Unit) {
//                val mealmanager = MealDataStoreManager(context)
//
//                val mealprefs = MealPreferences(
//                    breakfast = savedMeals[0],
//                    lunch = savedMeals[1],
//                    dinner = savedMeals[2],
//                    balance = savedMeals[3],
//                )
//                mealmanager.saveToDataStore(mealprefs)
//            }
//        }
    }

    private suspend fun loadPreferences(
        preferenceDataStore: DataStore<Preferences>,
        onLoaded: () -> Unit
    ): SettingsPreferences {
        val preferences = preferenceDataStore.data.first()

        val s = SettingsPreferences(
            language = preferences[LANGUAGE] ?: "",
            darktheme = preferences[DARK_THEME] ?: false,
            materialyoutheme = preferences[MATERIALYOU_THEME] ?: false,
            budget = preferences[BUDGET] ?: false,
            tokenwarning = preferences[TOKEN_WARNING] ?: 2,
            userID = preferences[USERID] ?: "",
        )
        onLoaded()
        return s
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