plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
}
buildscript {


    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath ("com.google.gms:google-services:4.3.15")
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48")

    }

}

