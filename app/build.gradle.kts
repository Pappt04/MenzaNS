plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.pappt04.menzans"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.pappt04.menzans"
        minSdk = 30
        targetSdk = 36
        versionCode = 10
        versionName = "1.0.$versionCode"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.navigation.compose)
    implementation(libs.androidx.navigation.fragment.compose)
    implementation(libs.androidx.material3)
    implementation(libs.gson)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation (libs.play.services.location)
    implementation(libs.androidx.appcompat)
    implementation(libs.play.services.maps)

    implementation(libs.compose)


    // Retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.okhttp.v490)

    // Coroutines
    implementation (libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android.v190)

    // Coroutine Lifecycle Scopes
    implementation (libs.androidx.lifecycle.viewmodel.ktx.v220)
    implementation (libs.androidx.lifecycle.runtime.ktx.v287)

    implementation(libs.logging.interceptor) // Or latest version
    implementation(libs.okhttp) // Or latest version
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // For `compose`. Creates a `ChartStyle` based on an M3 Material Theme.
    implementation(libs.compose.m3)

    // Houses the core logic for charts and other elements. Included in all other modules.
    implementation(libs.core)
    implementation(libs.material)

    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.material.icons.extended)

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.ui.text.google.fonts)

    implementation(libs.androidx.datastore.preferences.core)
    implementation(libs.androidx.datastore.core)

    implementation(libs.androidx.datastore.preferences.rxjava3)
    implementation(libs.androidx.datastore.rxjava3)

    implementation(libs.androidx.glance)
    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.glance.material)
    implementation(libs.glance.material3)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}