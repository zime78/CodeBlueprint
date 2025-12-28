package com.codeblueprint.data.local

import app.cash.sqldelight.db.SqlDriver

/**
 * 플랫폼별 SQLDelight 드라이버 팩토리 인터페이스
 *
 * 각 플랫폼(Android, iOS, Desktop)에서 실제 구현을 제공합니다.
 */
expect class DatabaseDriverFactory {
    /**
     * SQLDelight 드라이버 생성
     */
    fun createDriver(): SqlDriver
}
