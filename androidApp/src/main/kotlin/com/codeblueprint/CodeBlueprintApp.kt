package com.codeblueprint

import android.app.Application
import com.codeblueprint.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

/**
 * CodeBlueprint Android Application
 */
class CodeBlueprintApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger()
            androidContext(this@CodeBlueprintApp)
        }
    }
}
