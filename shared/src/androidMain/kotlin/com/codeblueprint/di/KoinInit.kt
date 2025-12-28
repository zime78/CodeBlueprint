package com.codeblueprint.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

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
}
