plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    alias(libs.plugins.ksp)
    id("com.android.library")
    id("org.jetbrains.compose")
}

kotlin {
    jvm()
    androidTarget()
    applyDefaultHierarchyTemplate()

//    val hostOs = System.getProperty("os.name")
//    val isArm64 = System.getProperty("os.arch") == "aarch64"
//    val isMingwX64 = hostOs.startsWith("Windows")
//    val nativeTarget = when {
//        hostOs == "Mac OS X" && isArm64 -> macosArm64()
//        hostOs == "Mac OS X" && !isArm64 -> macosX64()
//        hostOs == "Linux" && isArm64 -> linuxArm64()
//        hostOs == "Linux" && !isArm64 -> linuxX64()
//        isMingwX64 -> mingwX64()
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

//    listOf(
//        mingwX64(),
//        macosArm64(),
//        macosX64(),
//        linuxArm64(),
//        linuxX64()
//    ).forEach {
//        it.binaries {
//            executable {
//                entryPoint = "main"
//            }
//        }
//    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
            binaryOption("bundleId", "com.ain.embat")
        }
    }
    sourceSets {
        all {
            languageSettings {
                languageVersion = "2.0"
            }
        }
        commonMain.configure {
            dependencies {
                implementation(compose.runtime)
                implementation(libs.atomicfu)
                implementation(libs.koin.core)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.mvvm.core)
                implementation(libs.mvvm.flow)
                implementation(libs.kotlinx.serialization.json)

                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.runtimeSaveable)
                implementation(compose.runtime)
                implementation(compose.materialIconsExtended)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
            }
        }

        val mobileMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation(libs.shared.firebase.firestore)
                implementation(libs.shared.firebase.common)
            }
        }

        androidMain {
            dependsOn(mobileMain)
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
                api(libs.timber)
                api(libs.android.fragment)
            }
        }

        iosMain {
            dependsOn(mobileMain)
        }

        val androidInstrumentedTest by getting {
            dependencies {
                implementation(project.dependencies.platform(libs.compose.bom))
                implementation(libs.bundles.compose.test)
            }
        }

        val androidUnitTest by getting {
            dependencies {
                implementation(libs.bundles.android.test)
                implementation(libs.okhttp.mockwebserver)
            }
        }

        val jvmAndNative by creating {
            dependsOn(commonMain.get())
        }
        jvmMain {
            dependsOn(jvmAndNative)
        }
        nativeMain {
            dependsOn(jvmAndNative)
        }
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.ain.embat"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/main/res")

    lint {
        baseline = file("lint-baseline.xml")
    }

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
