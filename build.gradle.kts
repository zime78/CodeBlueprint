// Top-level build file for Kotlin Multiplatform project
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.sqldelight) apply false
    alias(libs.plugins.ksp) apply false
}

// 빌드 결과물 수집 태스크
tasks.register<Copy>("collectOutputs") {
    group = "distribution"
    description = "빌드 결과물을 build/outputs 폴더로 수집"

    // Desktop .app 번들
    from("desktopApp/build/compose/binaries/main/app") {
        into("desktop/app")
    }

    // Desktop DMG
    from("desktopApp/build/compose/binaries/main/dmg") {
        into("desktop/dmg")
    }

    // Desktop UberJar
    from("desktopApp/build/compose/jars") {
        into("desktop/jar")
    }

    // Android Debug APK
    from("androidApp/build/outputs/apk/debug") {
        into("android/debug")
        include("*.apk")
    }

    // Android Release APK
    from("androidApp/build/outputs/apk/release") {
        into("android/release")
        include("*.apk")
    }

    into(layout.buildDirectory.dir("outputs"))
}

// 전체 빌드 후 수집
tasks.register("buildAndCollect") {
    group = "distribution"
    description = "전체 빌드 후 결과물 수집"

    dependsOn(
        ":desktopApp:createDistributable",
        ":desktopApp:packageDmg",
        ":desktopApp:packageUberJarForCurrentOS",
        ":androidApp:assembleDebug",
        ":androidApp:assembleRelease"
    )
    finalizedBy("collectOutputs")
}

// outputs 폴더 정리
tasks.register<Delete>("cleanOutputs") {
    group = "distribution"
    description = "outputs 폴더 정리"
    delete(layout.buildDirectory.dir("outputs"))
}
