package com.pappt04.menzans.di

import com.pappt04.menzans.data.local.datastore.CardDataStoreManager
import com.pappt04.menzans.data.local.datastore.MealDataStoreManager
import com.pappt04.menzans.data.local.datastore.SettingsDataStoreManager
import com.pappt04.menzans.data.local.room.MenzaDatabase
import com.pappt04.menzans.repository.CardRepository
import com.pappt04.menzans.repository.GeofenceRepository
import com.pappt04.menzans.repository.MealRepository
import com.pappt04.menzans.repository.SettingsRepository
import com.pappt04.menzans.repository.StatisticsRepository
import com.pappt04.menzans.repository.UserRepository
import com.pappt04.menzans.repository.WaitTimeRepository
import com.pappt04.menzans.service.RetrofitClient
import com.pappt04.menzans.viewmodels.CardViewModel
import com.pappt04.menzans.viewmodels.DashboardViewModel
import com.pappt04.menzans.viewmodels.MainViewModel
import com.pappt04.menzans.viewmodels.SettingsViewModel
import com.pappt04.menzans.viewmodels.StatisticsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule: Module =
    module {
        // DataStore managers
        single { SettingsDataStoreManager(androidContext()) }
        single { MealDataStoreManager(androidContext()) }
        single { CardDataStoreManager(androidContext()) }

        // Database
        single { MenzaDatabase.create(androidContext()) }
        single { get<MenzaDatabase>().mealEventDao() }

        // Service
        single { RetrofitClient.apiService }

        // Repositories
        single { UserRepository(get(), get()) }
        single { MealRepository(get()) }
        single { CardRepository(get()) }
        single { SettingsRepository(get()) }
        single { WaitTimeRepository(get()) }
        single { StatisticsRepository(get(), get(), get()) }
        single { GeofenceRepository(get(), get(), androidContext()) }

        // ViewModels
        viewModel { MainViewModel(get(), get(), get()) }
        viewModel { DashboardViewModel(get(), get()) }
        viewModel { StatisticsViewModel(get()) }
        viewModel { CardViewModel(get()) }
        viewModel { SettingsViewModel(get()) }
    }
