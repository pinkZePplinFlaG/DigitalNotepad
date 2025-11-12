
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.woody.digitalnotepad"
//    buildFeatures {
//        compose = true
//    }
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.woody.digitalnotepad"
        minSdk = 33
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
//    // Specify the Compose BOM with a version definition
//    val composeBom = platform("androidx.compose:compose-bom:2025.10.01")
//    implementation(composeBom)
//    testImplementation(composeBom)
//    androidTestImplementation(composeBom)
//
//    // Specify Compose library dependencies without a version definition
//    implementation("androidx.compose.foundation:foundation")
//    // ..
//    testImplementation("androidx.compose.ui:ui-test-junit4")
//    // ..
//    androidTestImplementation("androidx.compose.ui:ui-test")
    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:34.5.0"))

    // Declare the dependency for the Cloud Firestore library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-firestore")
    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}