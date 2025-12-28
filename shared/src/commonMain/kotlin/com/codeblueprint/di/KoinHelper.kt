package com.codeblueprint.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module

/**
 * Koin 초기화 헬퍼
 *
 * 각 플랫폼에서 Koin을 초기화할 때 사용합니다.
 */
object KoinHelper {
    /**
     * Koin 초기화 (플랫폼 모듈 포함)
     */
    fun initKoin(platformModules: List<Module>) {
        startKoin {
            modules(commonModules() + platformModules)
        }
    }
}
