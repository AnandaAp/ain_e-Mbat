buildscript {
    dependencies {
        classpath(libs.firebase.common.ktx)
        classpath(libs.google.services)
    }
}
plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    kotlin("multiplatform").apply(false)
    id("com.android.application").apply(false)
    id("com.android.library").apply(false)
    id("org.jetbrains.compose").apply(false)
    alias(libs.plugins.google.services).apply(false)
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
}