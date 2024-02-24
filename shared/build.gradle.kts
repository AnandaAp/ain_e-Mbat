import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    alias(libs.plugins.ksp)
    alias(libs.plugins.moko.resource)
    id("dev.icerock.mobile.multiplatform.ios-framework")
    id("dev.icerock.mobile.multiplatform.cocoapods")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("kotlin-parcelize")
}

kotlin {
    targets
        .filterIsInstance<KotlinNativeTarget>()
        .flatMap { it.binaries }
        .filterIsInstance<Framework>()
        .forEach { framework ->
            framework.linkerOpts (
                project.file("../ios-app/Pods/TensorFlowLiteC/Frameworks").path.let { "-F$it" },
                "-framework",
                "TensorFlowLiteC"
            )
        }

    jvm()
    androidTarget()
    applyDefaultHierarchyTemplate()

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
            binaryOption("bundleId", "com.ain.embat")
        }
//        iosTarget.binaries.configureEach {
//            configurations.all {
//                exclude(group = "dev.icerock.moko", module = "tensorflow")
//            }
//        }
    }

    secrets {
        propertiesFileName = "secret.properties"
    }

    sourceSets {
        all {
            languageSettings {
                languageVersion = "2.0"
            }
        }
        commonMain.configure {
            kotlin.srcDirs("build/generated/ksp/main/kotlin")
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
                api(compose.animation)
                api(libs.bundles.precompose)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.runtimeSaveable)
                implementation(compose.runtime)
                implementation(compose.materialIconsExtended)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.client.logging)
                implementation(libs.kotlinx.logging.jvm)
                implementation (libs.bundles.tarsos)
                api(libs.bundles.constraintlayout)
                api(libs.google.generativeai)
            }
        }

        val mobileMain by creating {
            dependsOn(commonMain.get())
            dependencies {
                implementation(libs.bundles.moko)
                implementation(libs.shared.firebase.firestore)
                implementation(libs.shared.firebase.common)
            }
        }

        val mobileTest by creating {
            dependsOn(mobileMain)
            dependencies {
                implementation(libs.bundles.moko.test)
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
            configurations.all {
                exclude(group = "io.github.oshai", module = "kotlin-logging-jvm")
            }
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
            dependencies {
                implementation(compose.ui)
                implementation(compose.foundation)
            }
        }
        jvmMain {
            dependsOn(jvmAndNative)
            dependencies {
                implementation(compose.ui)
                implementation(compose.foundation)
            }
        }
        nativeMain {
            dependsOn(jvmAndNative)
            dependencies {
                implementation(compose.ui)
                implementation(compose.foundation)
            }
        }

//        val iosSimulatorArm64Main by getting {
//            configurations.all {
//                exclude(group = "dev.icerock.moko", module = "tensorflow")
//            }
//        }
//
//        val iosSimulatorArm64Test by getting {
//            configurations.all {
//                exclude(group = "dev.icerock.moko", module = "tensorflow")
//            }
//        }
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.ain.embat"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/main/res")

    buildFeatures {
        buildConfig = true
    }

    secrets {
        propertiesFileName = "secret.properties"
    }

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

ksp {
    arg("KOIN_CONFIG_CHECK","true")
}

multiplatformResources {
    multiplatformResourcesPackage = "library"
    multiplatformResourcesSourceSet = "mobileMain"
    disableStaticFrameworkWarning = true
}