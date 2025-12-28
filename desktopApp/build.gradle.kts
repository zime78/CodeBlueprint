import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    jvm("desktop") {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    sourceSets {
        val desktopMain by getting {
            dependencies {
                implementation(project(":shared"))
                implementation(project(":composeApp"))

                implementation(compose.desktop.currentOs)

                // Koin
                implementation(libs.koin.core)

                // Decompose
                implementation(libs.decompose)
                implementation(libs.decompose.extensions.compose)

                // Coroutines
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.coroutines.swing)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.codeblueprint.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)

            packageName = "CodeBlueprint"
            packageVersion = "1.0.0"
            description = "디자인 패턴 학습 앱"
            vendor = "CodeBlueprint"

            macOS {
                bundleID = "com.codeblueprint"
                iconFile.set(project.file("icons/icon.icns"))
            }

            windows {
                iconFile.set(project.file("icons/icon.ico"))
                menuGroup = "CodeBlueprint"
            }

            linux {
                iconFile.set(project.file("icons/icon.png"))
            }
        }
    }
}
