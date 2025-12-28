package com.codeblueprint.di

import com.codeblueprint.data.local.DatabaseDriverFactory
import com.codeblueprint.platform.ClipboardService
import org.koin.dsl.module

/**
 * Desktop 플랫폼 모듈
 *
 * Desktop(JVM) 전용 의존성 정의
 */
val platformModule = module {
    single { DatabaseDriverFactory() }
    single { ClipboardService() }
}

/**
 * Desktop용 전체 모듈 목록
 */
fun allModules() = commonModules() + platformModule
