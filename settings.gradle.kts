import java.net.URI

rootProject.name = "ain_e-Mbat"

include(":androidApp")
include(":shared")
include(":desktop")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        val kotlinVersion = extra["kotlin.version"] as String
        val agpVersion = extra["agp.version"] as String
        val composeVersion = extra["compose.version"] as String

        kotlin("jvm").version(kotlinVersion)
        kotlin("multiplatform").version(kotlinVersion)
        kotlin("android").version(kotlinVersion)

        id("com.android.application").version(agpVersion)
        id("com.android.library").version(agpVersion)
        id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin").version("2.0.1")
        id("org.jetbrains.compose").version(composeVersion)
        id("dev.icerock.mobile.multiplatform.ios-framework").version("0.14.2")
        id("dev.icerock.mobile.multiplatform.cocoapods").version("0.14.2")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.7.0")
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven {  // Only for snapshot artifacts
            name = "ossrh-snapshot"
            url = URI("https://oss.sonatype.org/content/repositories/snapshots")
        }
        maven(url = "https://mvn.0110.be/releases") {
            name = "TarsosDSP repository"
        }
    }
}
