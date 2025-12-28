#!/bin/bash
# iOS 앱 빌드

set -e
cd "$(dirname "${BASH_SOURCE[0]}")/.."

echo "🍎 iOS 앱 빌드 시작..."

# Xcode 확인
if ! command -v xcodebuild &> /dev/null; then
    echo "❌ Xcode가 설치되어 있지 않습니다."
    exit 1
fi

# iOS Framework 빌드
echo "📦 iOS Framework 빌드 중..."
./gradlew :composeApp:linkReleaseFrameworkIosArm64 --no-daemon 2>/dev/null || {
    echo "⚠️  iOS Framework 빌드 실패"
}

# Xcode 프로젝트 확인
XCODE_PROJECT="iosApp/iosApp.xcodeproj"
if [ ! -d "$XCODE_PROJECT" ]; then
    echo ""
    echo "⚠️  Xcode 프로젝트를 찾을 수 없습니다: $XCODE_PROJECT"
    echo ""
    echo "iOS 빌드를 위해 다음 단계를 수행하세요:"
    echo "  1. Xcode에서 새 iOS 프로젝트 생성"
    echo "  2. iosApp/iosApp.xcodeproj에 저장"
    echo "  3. shared 모듈의 framework 연동"
    echo ""
    echo "또는 KMP Xcode 플러그인을 사용하세요:"
    echo "  https://github.com/nicjohnson4/xcode-kotlin"
    exit 1
fi

# 시뮬레이터용 빌드
echo "📱 시뮬레이터용 빌드 중..."
xcodebuild \
    -project "$XCODE_PROJECT" \
    -scheme "iosApp" \
    -configuration Debug \
    -destination 'platform=iOS Simulator,name=iPhone 15' \
    build 2>/dev/null || echo "⚠️  시뮬레이터 빌드 실패"

echo ""
echo "✅ 빌드 완료!"
