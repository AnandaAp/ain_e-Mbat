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
}