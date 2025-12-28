package com.codeblueprint.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * CodeBlueprint 앱의 Application 클래스
 *
 * Hilt를 통한 의존성 주입의 진입점 역할을 수행합니다.
 */
@HiltAndroidApp
class CodeBlueprintApp : Application()
