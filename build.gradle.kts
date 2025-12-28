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

    dependsOn("cleanOutputs")
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

// ===========================================
// Pre-built Database 생성 태스크
// ===========================================

/**
 * Seed 데이터베이스 생성
 *
 * desktopApp을 --generate-seed-db 모드로 실행하여 Pre-built DB를 생성합니다.
 * 생성된 DB는 shared/src/desktopMain/resources에 저장됩니다.
 */
tasks.register<JavaExec>("generateSeedDatabase") {
    group = "database"
    description = "Pre-built Seed 데이터베이스 생성"

    dependsOn(":desktopApp:compileKotlinDesktop")

    val desktopApp = project(":desktopApp")

    mainClass.set("com.codeblueprint.MainKt")

    doFirst {
        // 리소스 디렉토리 생성
        file("${rootProject.projectDir}/shared/src/desktopMain/resources").mkdirs()

        // classpath 설정
        val runtimeClasspath = desktopApp.configurations.getByName("desktopRuntimeClasspath")
        val compiledClasses = desktopApp.layout.buildDirectory.dir("classes/kotlin/desktop/main").get().asFile
        classpath = files(compiledClasses) + runtimeClasspath
    }

    args = listOf(
        "--generate-seed-db",
        "${rootProject.projectDir}/shared/src/desktopMain/resources/codeblueprint_seed.db"
    )

    doLast {
        val seedDb = file("${rootProject.projectDir}/shared/src/desktopMain/resources/codeblueprint_seed.db")
        if (seedDb.exists()) {
            println("✅ Seed database generated successfully!")
            println("   Location: ${seedDb.absolutePath}")
            println("   Size: ${seedDb.length() / 1024} KB")
        } else {
            throw GradleException("Failed to generate seed database")
        }
    }
}

/**
 * Android용 Seed DB 복사
 *
 * Desktop용으로 생성된 Seed DB를 Android assets 폴더로 복사합니다.
 */
tasks.register<Copy>("copySeedDbToAndroid") {
    group = "database"
    description = "Seed DB를 Android assets로 복사"

    dependsOn("generateSeedDatabase")

    from("shared/src/desktopMain/resources/codeblueprint_seed.db")
    into("androidApp/src/main/assets/databases")

    doFirst {
        file("androidApp/src/main/assets/databases").mkdirs()
    }
}

/**
 * 전체 Seed DB 생성 및 배포
 */
tasks.register("prepareSeedDatabase") {
    group = "database"
    description = "모든 플랫폼용 Seed DB 생성 및 배포"

    dependsOn("generateSeedDatabase", "copySeedDbToAndroid")

    doLast {
        println("✅ Seed database prepared for all platforms!")
        println("   Desktop: shared/src/desktopMain/resources/codeblueprint_seed.db")
        println("   Android: androidApp/src/main/assets/databases/codeblueprint_seed.db")
        println("   iOS: Bundle에 수동 추가 필요 (Xcode에서 codeblueprint_seed.db 추가)")
    }
}
