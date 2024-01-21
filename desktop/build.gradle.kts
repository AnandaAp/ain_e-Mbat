import org.jetbrains.compose.desktop.application.dsl.TargetFormat
plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.ain.embat"
version = "1.0"

kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
    }
    sourceSets {
        jvmMain {
            configurations.all {
                exclude(group = "dev.icerock.moko")
            }
            dependencies {
                implementation(project(":shared"))
                implementation(compose.desktop.currentOs)
                implementation(compose.material3)
                implementation(libs.skiko.awt.runtime.windows.x64)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        jvmArgs += listOf("-Xmx2G")
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            appResourcesRootDir.set(project.layout.projectDirectory.dir("resources"))
            packageName = "EMbat"
            packageVersion = "1.0.0"
        }
    }
}