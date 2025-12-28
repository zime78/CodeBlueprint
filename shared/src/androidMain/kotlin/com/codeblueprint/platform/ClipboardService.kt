package com.codeblueprint.platform

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

/**
 * Android 클립보드 서비스 구현
 *
 * Android ClipboardManager를 사용하여 클립보드에 접근
 */
actual class ClipboardService(private val context: Context) {
    /**
     * 텍스트를 클립보드에 복사
     *
     * @param text 복사할 텍스트
     * @return 복사 성공 여부
     */
    actual fun copyToClipboard(text: String): Boolean {
        return try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("code", text)
            clipboard.setPrimaryClip(clip)
            true
        } catch (e: Exception) {
            false
        }
    }
}
