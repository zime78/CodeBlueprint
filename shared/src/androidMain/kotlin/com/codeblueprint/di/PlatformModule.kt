package com.codeblueprint.di

import com.codeblueprint.data.local.DatabaseDriverFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Android 플랫폼 모듈
 *
 * Android 전용 의존성 정의 (Context 필요한 것들)
 */
val platformModule = module {
    single { DatabaseDriverFactory(androidContext()) }
}

/**
 * Android용 전체 모듈 목록
 */
fun allModules() = commonModules() + platformModule
