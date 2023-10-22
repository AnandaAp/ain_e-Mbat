plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.9.0"
    id("com.android.library")
    id("org.jetbrains.compose")
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
}

kotlin {
    androidTarget()

//    val hostOs = System.getProperty("os.name")
//    val isArm64 = System.getProperty("os.arch") == "aarch64"
//    val isMingwX64 = hostOs.startsWith("Windows")
//    val nativeTarget = when {
//        hostOs == "Mac OS X" && isArm64 -> macosArm64("native")
//        hostOs == "Mac OS X" && !isArm64 -> macosX64("native")
//        hostOs == "Linux" && isArm64 -> linuxArm64("native")
//        hostOs == "Linux" && !isArm64 -> linuxX64("native")
//        isMingwX64 -> mingwX64("native")
//        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
//    }
//
//    nativeTarget.apply {
//        binaries {
//            executable {
//                entryPoint = "main"
//            }
//        }
//    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.runtimeSaveable)
                implementation(compose.runtime)
                implementation(compose.materialIconsExtended)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation(libs.atomicfu)
                implementation(libs.shared.firebase.firestore)
                implementation(libs.shared.firebase.common)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.koin.core)
                api(libs.kotlinx.coroutines.core)
                api(libs.mvvm.core)
                api(libs.mvvm.flow)
                api (libs.timber)
            }
        }
        val androidMain by getting {
            dependencies {
                api(libs.bundles.android.compose)
                api(libs.bundles.android.lifecycle)
                api(libs.shared.firebase.firestore)
                api(libs.shared.firebase.common)
                api(libs.firebase.firestore.ktx)
                api(libs.koin.android)
                api(libs.lifecycle.viewmodel.ktx)
                api(libs.lifecycle.viewmodel.compose)
                api(libs.mvvm.flow.compose)
                api(libs.lifecycle.runtime.compose)
                api(libs.bundles.okhttp)
                api(libs.bundles.coil)
                api(libs.conscrypt.android)
            }
        }
        val androidInstrumentedTest by getting {
            dependencies {
                implementation(platform("androidx.compose:compose-bom:2023.03.00"))
                implementation("androidx.compose.ui:ui-test-junit4")
                implementation("androidx.compose.ui:ui-tooling")
                implementation("androidx.compose.ui:ui-test-manifest")

            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(libs.bundles.android.test)
                implementation(libs.okhttp.mockwebserver)
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }

//        val nativeMain by getting {
//            dependencies {
//                api(compose.desktop.currentOs)
//            }
//        }
//        val nativeTest by getting
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.myapplication.common"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/main/res")

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
}
