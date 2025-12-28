package com.codeblueprint.platform

import platform.UIKit.UIPasteboard

/**
 * iOS 클립보드 서비스 구현
 *
 * UIPasteboard를 사용하여 클립보드에 접근
 */
actual class ClipboardService {
    /**
     * 텍스트를 클립보드에 복사
     *
     * @param text 복사할 텍스트
     * @return 복사 성공 여부
     */
    actual fun copyToClipboard(text: String): Boolean {
        return try {
            UIPasteboard.generalPasteboard.string = text
            true
        } catch (e: Exception) {
            false
        }
    }
}
