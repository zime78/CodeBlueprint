package com.codeblueprint.di

import com.codeblueprint.data.local.AlgorithmDataProvider
import com.codeblueprint.data.local.DatabaseDriverFactory
import com.codeblueprint.data.local.PatternDataInitializer
import kotlinx.coroutines.runBlocking
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatformTools

/**
 * Android Koin 초기화
 *
 * Application에서 호출합니다.
 */
fun initKoin(appDeclaration: KoinApplication.() -> Unit) {
    startKoin {
        appDeclaration()
        modules(allModules())
    }

    // Seed DB에서 초기화 시도
    val seedInitialized = initializeFromSeed()

    // Seed DB가 없거나 초기화 실패 시 런타임 초기화로 fallback
    if (!seedInitialized) {
        initializePatternData()
        initializeAlgorithmData()
    }
}

/**
 * Seed DB에서 초기화
 *
 * Pre-built DB 파일이 있으면 앱 데이터 폴더로 복사합니다.
 * @return true: Seed DB로 초기화됨, false: fallback 필요
 */
private fun initializeFromSeed(): Boolean {
    val koin = KoinPlatformTools.defaultContext().get()
    val dbFactory: DatabaseDriverFactory = koin.get()

    return runBlocking {
        dbFactory.initializeFromSeedIfNeeded()
    }
}

/**
 * 패턴 데이터 초기화 (Seed DB 없을 때 fallback)
 *
 * DB에 패턴 데이터가 없으면 23개 GoF 패턴을 삽입합니다.
 */
private fun initializePatternData() {
    val koin = KoinPlatformTools.defaultContext().get()
    val initializer: PatternDataInitializer = koin.get()

    runBlocking {
        initializer.initializeIfNeeded()
    }
}

/**
 * 알고리즘 데이터 초기화 (Seed DB 없을 때 fallback)
 *
 * DB에 알고리즘 데이터가 없으면 73개 알고리즘을 삽입합니다.
 */
private fun initializeAlgorithmData() {
    val koin = KoinPlatformTools.defaultContext().get()
    val provider: AlgorithmDataProvider = koin.get()

    runBlocking {
        provider.initializeIfNeeded()
    }
}
