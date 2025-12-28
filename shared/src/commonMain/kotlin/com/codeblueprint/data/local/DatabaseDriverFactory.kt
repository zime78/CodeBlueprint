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

    /**
     * Seed DB에서 초기화 (필요시)
     *
     * 첫 실행 시 Seed DB를 사용자 폴더로 복사하고,
     * 기존 DB가 있으면 버전 체크 후 마이그레이션을 수행합니다.
     *
     * @return true: 초기화/마이그레이션 수행됨, false: 기존 DB 사용
     */
    suspend fun initializeFromSeedIfNeeded(): Boolean
}
