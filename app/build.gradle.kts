

plugins {
    alias(libs.plugins.android.application) // This alias should match your libs.versions.toml
    alias(libs.plugins.jetbrains.kotlin.android) // Ensure the alias exists in your libs.versions.toml
    id("com.google.gms.google-services") // Firebase plugin
    id("kotlin-android")
    id("kotlin-kapt")
    id ("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.compensation_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.compensation_app"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}" // Correct wildcard exclusion syntax
            excludes += "META-INF/versions/9/OSGI-INF/MANIFEST.MF" // Fixed key from exclude to excludes
        }
    }
}
dependencies {
    implementation ("com.itextpdf:itextpdf:5.5.13.2")
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.30.1")
    implementation( "androidx.room:room-runtime:2.6.1")
    implementation(libs.androidx.room.common)
    kapt ("androidx.room:room-compiler:2.6.1") // Use kapt for Kotlin
    implementation ("androidx.room:room-ktx:2.6.1") // Kotlin Extensions

//location
  //  implementation ("com.google.android.gms:play-services-maps:18.1.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation( "com.google.accompanist:accompanist-permissions:0.33.2-alpha")

    implementation("com.google.maps.android:maps-compose:2.14.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    //implementation("com.google.android.gms:play-services-location:21.0.1")


    implementation("io.coil-kt:coil-compose:2.5.0")
    // Retrofit and OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    // Firebase BoM - Centralized dependency management for Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))

    // Firebase Core Libraries
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-appcheck-playintegrity")
    implementation ("com.google.firebase:firebase-storage-ktx")
    implementation ("com.google.firebase:firebase-firestore-ktx")

    // Uncomment Firestore only if you're using it
    // implementation("com.google.firebase:firebase-firestore-ktx")
    implementation ("com.google.code.gson:gson:2.10.1" )// Check for the latest version
    // Dagger Hilt for Dependency Injection
    implementation("com.google.dagger:hilt-android:2.48")
    implementation(libs.androidx.appcompat)
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // AndroidX Core Libraries
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")

    // Jetpack Compose Libraries
    implementation("androidx.compose.ui:ui:1.5.1") // Use stable version
    implementation("androidx.compose.material3:material3:1.1.0") // Stable version
    implementation("androidx.navigation:navigation-compose:2.7.3")
    implementation("androidx.compose.ui:ui-tooling:1.5.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.1")

    // Testing Dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.5.1")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.1")

    // Remove redundant aliases and unused dependencies
    // If you're using libs.versions.toml, ensure that only relevant aliases are defined.

    implementation ("com.google.android.gms:play-services-location:21.3.0")
    implementation ("com.google.android.gms:play-services-maps:19.0.0")




}
