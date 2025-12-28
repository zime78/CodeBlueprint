package com.codeblueprint.platform

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

/**
 * Desktop(JVM) 클립보드 서비스 구현
 *
 * AWT Toolkit을 사용하여 시스템 클립보드에 접근
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
            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
            val selection = StringSelection(text)
            clipboard.setContents(selection, null)
            true
        } catch (e: Exception) {
            false
        }
    }
}
