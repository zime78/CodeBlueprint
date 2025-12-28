#!/bin/bash
# Android 앱 빌드

set -e
cd "$(dirname "${BASH_SOURCE[0]}")/.."

echo "🤖 Android 앱 빌드 시작..."

# Debug APK 빌드
echo "📦 Debug APK 빌드 중..."
./gradlew :androidApp:assembleDebug --no-daemon

# Release APK 빌드 (서명 키 필요)
echo "📦 Release APK 빌드 중..."
./gradlew :androidApp:assembleRelease --no-daemon 2>/dev/null || echo "⚠️  Release 빌드 실패 (서명 키 필요)"

echo ""
echo "✅ 빌드 완료!"
echo ""
echo "📁 Debug APK: androidApp/build/outputs/apk/debug/"
ls -la androidApp/build/outputs/apk/debug/*.apk 2>/dev/null || true
echo ""
echo "📁 Release APK: androidApp/build/outputs/apk/release/"
ls -la androidApp/build/outputs/apk/release/*.apk 2>/dev/null || true
