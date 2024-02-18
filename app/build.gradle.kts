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
        minSdk = 33
        // noinspection OldTargetApi, EditedTargetSdkVersion
        targetSdk = 33
        compileSdk = 34
        versionCode = 3
        versionName = "1.0.2"
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
    implementation("androidx.core:core-ktx:1.12.0")
    // AppCompat
    implementation("androidx.appcompat:appcompat:1.6.1")
    // ConstraintLayout
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    // Material
    implementation("com.google.android.material:material:1.11.0")
    // Kotlinx Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-common:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
}