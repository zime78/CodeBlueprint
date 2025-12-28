package com.codeblueprint.di

import org.koin.core.context.startKoin

/**
 * iOS Koin 초기화
 *
 * Swift에서 호출하기 위한 함수
 */
fun initKoin() {
    startKoin {
        modules(allModules())
    }
}
