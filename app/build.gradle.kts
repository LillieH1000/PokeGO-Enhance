plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "h.lillie.pokegoenhance"
    compileSdkPreview = "VanillaIceCream"

    defaultConfig {
        applicationId = "h.lillie.pokegoenhance"
        minSdk = 33
        // noinspection OldTargetApi, EditedTargetSdkVersion
        targetSdkPreview = "VanillaIceCream"
        versionCode = 13
        versionName = "1.1.2"
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
    val coreVersion = "1.13.0"
    implementation("androidx.core:core-ktx:$coreVersion")

    // AppCompat
    val appcompatVersion = "1.6.1"
    implementation("androidx.appcompat:appcompat:$appcompatVersion")

    // ConstraintLayout
    val constraintlayoutVersion = "2.1.4"
    implementation("androidx.constraintlayout:constraintlayout:$constraintlayoutVersion")

    // Material
    val materialVersion = "1.11.0"
    implementation("com.google.android.material:material:$materialVersion")

    // Kotlinx Coroutines
    val kotlinxCoroutinesVersion = "1.8.0"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinxCoroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")

    // Lifecycle
    val lifecycleVersion = "2.7.0"
    implementation("androidx.lifecycle:lifecycle-common:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
}