plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "h.lillie.pokegotouch"
    compileSdk = 34
    ndkVersion = "26.1.10909125"

    defaultConfig {
        applicationId = "h.lillie.pokegotouch"
        minSdk = 30
        // noinspection OldTargetApi,EditedTargetSdkVersion
        targetSdk = 33
        compileSdk = 34
        versionCode = 1
        versionName = "1.0.0"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
        }
        release {
            isMinifyEnabled = false
            isDebuggable = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core
    val core_version = "1.12.0"
    implementation("androidx.core:core-ktx:$core_version")

    // AppCompat
    val appcompat_version = "1.6.1"
    implementation("androidx.appcompat:appcompat:$appcompat_version")

    // ConstraintLayout
    val constraintlayout_version = "2.1.4"
    implementation("androidx.constraintlayout:constraintlayout:$constraintlayout_version")

    // Material
    val material_version = "1.11.0"
    implementation("com.google.android.material:material:$material_version")

    // Kotlinx Coroutines
    val kotlinx_coroutines_version = "1.7.3"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinx_coroutines_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinx_coroutines_version")

    // Lifecycle
    val lifecycle_version = "2.7.0"
    implementation("androidx.lifecycle:lifecycle-common:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
}