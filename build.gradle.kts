plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    kotlin("multiplatform").apply(false)
    kotlin("plugin.serialization") version libs.versions.kotlinx.serialization.plugin.get() apply false
    kotlin("jvm").apply(false)
    id("com.android.application").apply(false)
    id("com.android.library").apply(false)
    id("org.jetbrains.compose").apply(false)
    alias(libs.plugins.google.services).apply(false)
    alias(libs.plugins.ksp).apply(false)
    alias(libs.plugins.moko.resource).apply(false)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin").apply(false)
    id("dev.icerock.mobile.multiplatform.cocoapods").apply(false)
}