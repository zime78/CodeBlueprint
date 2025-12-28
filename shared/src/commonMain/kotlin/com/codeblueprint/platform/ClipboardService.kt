package com.codeblueprint.platform

/**
 * 클립보드 서비스
 *
 * 플랫폼별 클립보드 접근을 위한 expect class
 */
expect class ClipboardService {
    /**
     * 텍스트를 클립보드에 복사
     *
     * @param text 복사할 텍스트
     * @return 복사 성공 여부
     */
    fun copyToClipboard(text: String): Boolean
}
