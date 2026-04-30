plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.nysae.calcu"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.nysae.calcu"
        minSdk = 24
        targetSdk = 35
        versionCode = 12
        versionName = "2.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    // Activity Compose (setContent)
    implementation("androidx.activity:activity-compose:1.8.2")

    // Compose UI
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.4")

    // Material 3
    implementation("androidx.compose.material3:material3:1.1.2")

    // Íconos extendidos
    implementation("androidx.compose.material:material-icons-extended:1.5.4")

    // ⭐ Necesario para compositionLocalOf
    implementation("androidx.compose.runtime:runtime:1.5.4")

    // ⭐ Necesario para viewModel() en Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    // AdMob
    implementation("com.google.android.gms:play-services-ads:23.1.0")

    // Billing
    implementation("com.android.billingclient:billing-ktx:6.1.0")
    //firebase
    implementation("com.google.firebase:firebase-analytics-ktx:21.6.1")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.4")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.4")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.4")
}