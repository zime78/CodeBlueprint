package com.codeblueprint.di

import com.codeblueprint.data.local.AlgorithmDataProvider
import com.codeblueprint.data.local.PatternDataInitializer
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatformTools

/**
 * Desktop Koin 초기화
 *
 * Koin을 시작하고 패턴/알고리즘 데이터를 초기화합니다.
 */
fun initKoin() {
    startKoin {
        modules(allModules())
    }

    // 패턴 및 알고리즘 데이터 초기화
    initializePatternData()
    initializeAlgorithmData()
}

/**
 * 패턴 데이터 초기화
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
 * 알고리즘 데이터 초기화
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
