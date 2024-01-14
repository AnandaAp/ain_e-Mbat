plugins {
    kotlin("multiplatform")
    id("com.android.application")
    id("org.jetbrains.compose")
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    alias(libs.plugins.ksp)
}

kotlin {
    androidTarget()
    sourceSets {
        androidMain {
            dependencies {
                implementation(project(":shared"))
                api(libs.bundles.tensorflow)
                api(libs.compose.ui.tooling.preview)
            }
        }
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.ain.embat"

    buildFeatures {
        buildConfig = true
    }

    secrets {
        propertiesFileName = "secret.properties"
    }

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    defaultConfig {
        applicationId = "com.ain.embat"
        minSdk = (findProperty("android.minSdk") as String).toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()
        versionCode = 1
        versionName = "1.0"
    }

    lint {
        baseline = file("lint-baseline.xml")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = true
    }
    kotlin {
        jvmToolchain(17)
    }

    signingConfigs {
        register("prod") {
            storeFile = file(extra["KEY_STORE_FILE_PROD"] as String)
            storePassword = extra["KEY_STORE_PASSWORD_PROD"] as String
            keyAlias = extra["KEY_STORE_ALIAS_PROD"] as String
            keyPassword = extra["KEY_STORE_PASSWORD_PROD"] as String
        }
        register("uat") {
            storeFile = file(extra["KEY_STORE_FILE_UAT"] as String)
            storePassword = extra["KEY_STORE_PASSWORD_UAT"] as String
            keyAlias = extra["KEY_STORE_ALIAS_UAT"] as String
            keyPassword = extra["KEY_STORE_PASSWORD_UAT"] as String
        }
    }

    flavorDimensions.add("server")
    productFlavors {
        register("uat") {
            applicationId = "com.ain.embat"
            dimension = "server"
        }
        register("prod") {
            applicationId = "com.ain.embat"
            dimension = "server"
            signingConfigs.getByName("prod")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles (getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("uat")
            initWith (getByName("release"))
            matchingFallbacks.add("release")
        }
        debug {
            isDebuggable = true
            proguardFiles (getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            initWith (getByName("debug"))
            matchingFallbacks.add("debug")
        }
    }
}
