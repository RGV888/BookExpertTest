// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.compose.compiler) apply false
//2.0.21-1.0.28
    //id("com.google.devtools.ksp") version "2.0.21-1.0.28" apply false
    id("com.google.devtools.ksp") version "2.0.0-1.0.23" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
}