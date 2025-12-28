package com.codeblueprint.domain.model

/**
 * 학습 진도 도메인 모델
 *
 * 사용자의 패턴별 학습 상태를 추적합니다.
 *
 * @property patternId 패턴 ID
 * @property isCompleted 학습 완료 여부
 * @property lastViewedAt 마지막 조회 시간 (Unix timestamp)
 * @property notes 사용자 메모
 * @property isBookmarked 북마크 여부
 */
data class LearningProgress(
    val patternId: String,
    val isCompleted: Boolean = false,
    val lastViewedAt: Long = System.currentTimeMillis(),
    val notes: String? = null,
    val isBookmarked: Boolean = false
)
