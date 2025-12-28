#!/bin/bash

# ============================================================
# CodeBlueprint 빌드 스크립트
#
# 사용법:
#   ./scripts/build.sh [옵션]
#
# 옵션:
#   all       - 모든 플랫폼 빌드
#   mac       - Mac 앱 빌드 (DMG)
#   android   - Android 앱 빌드 (APK + AAB)
#   ios       - iOS 앱 빌드
#   clean     - 빌드 캐시 정리
#   help      - 도움말 표시
# ============================================================

set -e

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 프로젝트 루트 디렉토리
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$PROJECT_ROOT"

# 출력 디렉토리
OUTPUT_DIR="$PROJECT_ROOT/build/outputs"
MAC_OUTPUT="$OUTPUT_DIR/mac"
ANDROID_OUTPUT="$OUTPUT_DIR/android"
IOS_OUTPUT="$OUTPUT_DIR/ios"

# 버전 정보
VERSION="1.0.0"
BUILD_DATE=$(date '+%Y-%m-%d %H:%M:%S')

# ============================================================
# 유틸리티 함수
# ============================================================

print_header() {
    echo ""
    echo -e "${BLUE}============================================================${NC}"
    echo -e "${BLUE} $1${NC}"
    echo -e "${BLUE}============================================================${NC}"
    echo ""
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_info() {
    echo -e "${BLUE}ℹ $1${NC}"
}

# 출력 디렉토리 생성
create_output_dirs() {
    mkdir -p "$MAC_OUTPUT"
    mkdir -p "$ANDROID_OUTPUT"
    mkdir -p "$IOS_OUTPUT"
}

# ============================================================
# Mac 빌드
# ============================================================

build_mac() {
    print_header "Mac 앱 빌드 (DMG)"

    print_info "Desktop 앱 빌드 중..."
    ./gradlew :desktopApp:packageDmg --no-daemon

    # DMG 파일 복사
    DMG_SOURCE="$PROJECT_ROOT/desktopApp/build/compose/binaries/main/dmg"
    if [ -d "$DMG_SOURCE" ]; then
        cp -r "$DMG_SOURCE"/*.dmg "$MAC_OUTPUT/" 2>/dev/null || true
        print_success "DMG 파일 생성 완료: $MAC_OUTPUT"
        ls -la "$MAC_OUTPUT"/*.dmg 2>/dev/null || true
    else
        print_warning "DMG 파일을 찾을 수 없습니다. 아이콘 파일이 필요할 수 있습니다."
        print_info "아이콘 없이 앱 실행: ./gradlew :desktopApp:run"
    fi

    echo ""
}

# ============================================================
# Android 빌드
# ============================================================

build_android() {
    print_header "Android 앱 빌드"

    # Debug APK 빌드
    print_info "Debug APK 빌드 중..."
    ./gradlew :androidApp:assembleDebug --no-daemon

    # Release APK 빌드
    print_info "Release APK 빌드 중..."
    ./gradlew :androidApp:assembleRelease --no-daemon || {
        print_warning "Release APK 빌드 실패 (서명 키 필요)"
    }

    # AAB 빌드 (Play Store 배포용)
    print_info "Release AAB 빌드 중..."
    ./gradlew :androidApp:bundleRelease --no-daemon || {
        print_warning "Release AAB 빌드 실패 (서명 키 필요)"
    }

    # APK 파일 복사
    APK_DEBUG="$PROJECT_ROOT/androidApp/build/outputs/apk/debug"
    APK_RELEASE="$PROJECT_ROOT/androidApp/build/outputs/apk/release"
    AAB_RELEASE="$PROJECT_ROOT/androidApp/build/outputs/bundle/release"

    if [ -d "$APK_DEBUG" ]; then
        cp -r "$APK_DEBUG"/*.apk "$ANDROID_OUTPUT/" 2>/dev/null || true
    fi

    if [ -d "$APK_RELEASE" ]; then
        cp -r "$APK_RELEASE"/*.apk "$ANDROID_OUTPUT/" 2>/dev/null || true
    fi

    if [ -d "$AAB_RELEASE" ]; then
        cp -r "$AAB_RELEASE"/*.aab "$ANDROID_OUTPUT/" 2>/dev/null || true
    fi

    print_success "Android 빌드 완료: $ANDROID_OUTPUT"
    ls -la "$ANDROID_OUTPUT"/ 2>/dev/null || true

    echo ""
}

# ============================================================
# iOS 빌드
# ============================================================

build_ios() {
    print_header "iOS 앱 빌드"

    # Xcode 확인
    if ! command -v xcodebuild &> /dev/null; then
        print_error "Xcode가 설치되어 있지 않습니다."
        return 1
    fi

    # iOS Framework 빌드
    print_info "iOS Framework 빌드 중..."
    ./gradlew :composeApp:linkReleaseFrameworkIosArm64 --no-daemon || {
        print_warning "iOS Framework 빌드 실패"
    }

    # XCode 프로젝트 경로 확인
    XCODE_PROJECT="$PROJECT_ROOT/iosApp/iosApp.xcodeproj"
    XCODE_WORKSPACE="$PROJECT_ROOT/iosApp/iosApp.xcworkspace"

    if [ -d "$XCODE_WORKSPACE" ]; then
        XCODE_PATH="$XCODE_WORKSPACE"
        BUILD_FLAG="-workspace"
    elif [ -d "$XCODE_PROJECT" ]; then
        XCODE_PATH="$XCODE_PROJECT"
        BUILD_FLAG="-project"
    else
        print_warning "Xcode 프로젝트를 찾을 수 없습니다."
        print_info "iOS 빌드를 위해 Xcode 프로젝트를 생성해주세요:"
        print_info "  1. Xcode에서 새 프로젝트 생성"
        print_info "  2. shared framework 연동"
        print_info "  3. iosApp/iosApp.xcodeproj에 저장"
        return 1
    fi

    # 시뮬레이터용 빌드
    print_info "iOS 시뮬레이터용 빌드 중..."
    xcodebuild \
        $BUILD_FLAG "$XCODE_PATH" \
        -scheme "iosApp" \
        -configuration Release \
        -destination 'generic/platform=iOS Simulator' \
        -derivedDataPath "$IOS_OUTPUT/DerivedData" \
        build || {
            print_warning "시뮬레이터 빌드 실패"
        }

    # 디바이스용 빌드 (아카이브)
    print_info "iOS 디바이스용 아카이브 중..."
    xcodebuild \
        $BUILD_FLAG "$XCODE_PATH" \
        -scheme "iosApp" \
        -configuration Release \
        -destination 'generic/platform=iOS' \
        -archivePath "$IOS_OUTPUT/iosApp.xcarchive" \
        archive || {
            print_warning "디바이스 아카이브 실패 (개발자 인증서 필요)"
        }

    # IPA 생성 (ExportOptions.plist 필요)
    EXPORT_OPTIONS="$PROJECT_ROOT/iosApp/ExportOptions.plist"
    if [ -f "$EXPORT_OPTIONS" ] && [ -d "$IOS_OUTPUT/iosApp.xcarchive" ]; then
        print_info "IPA 생성 중..."
        xcodebuild \
            -exportArchive \
            -archivePath "$IOS_OUTPUT/iosApp.xcarchive" \
            -exportPath "$IOS_OUTPUT" \
            -exportOptionsPlist "$EXPORT_OPTIONS" || {
                print_warning "IPA 생성 실패"
            }
    fi

    print_success "iOS 빌드 완료: $IOS_OUTPUT"
    ls -la "$IOS_OUTPUT"/ 2>/dev/null || true

    echo ""
}

# ============================================================
# 전체 빌드
# ============================================================

build_all() {
    print_header "모든 플랫폼 빌드"

    build_mac
    build_android
    build_ios

    print_header "빌드 완료 요약"
    echo "출력 디렉토리: $OUTPUT_DIR"
    echo ""
    echo "Mac:     $MAC_OUTPUT"
    echo "Android: $ANDROID_OUTPUT"
    echo "iOS:     $IOS_OUTPUT"
}

# ============================================================
# 정리
# ============================================================

clean_build() {
    print_header "빌드 캐시 정리"

    print_info "Gradle 캐시 정리 중..."
    ./gradlew clean --no-daemon

    print_info "출력 디렉토리 정리 중..."
    rm -rf "$OUTPUT_DIR"

    print_success "정리 완료"
}

# ============================================================
# 도움말
# ============================================================

show_help() {
    echo ""
    echo "CodeBlueprint 빌드 스크립트 v$VERSION"
    echo ""
    echo "사용법: ./scripts/build.sh [옵션]"
    echo ""
    echo "옵션:"
    echo "  all       모든 플랫폼 빌드 (Mac, Android, iOS)"
    echo "  mac       Mac 앱 빌드 (DMG 패키지)"
    echo "  android   Android 앱 빌드 (APK, AAB)"
    echo "  ios       iOS 앱 빌드 (시뮬레이터, 디바이스)"
    echo "  clean     빌드 캐시 정리"
    echo "  help      이 도움말 표시"
    echo ""
    echo "예시:"
    echo "  ./scripts/build.sh mac       # Mac DMG 빌드"
    echo "  ./scripts/build.sh android   # Android APK/AAB 빌드"
    echo "  ./scripts/build.sh all       # 모든 플랫폼 빌드"
    echo ""
    echo "출력 위치:"
    echo "  Mac:     build/outputs/mac/"
    echo "  Android: build/outputs/android/"
    echo "  iOS:     build/outputs/ios/"
    echo ""
}

# ============================================================
# 메인
# ============================================================

main() {
    print_header "CodeBlueprint 빌드 스크립트"
    echo "버전: $VERSION"
    echo "빌드 시간: $BUILD_DATE"
    echo "프로젝트: $PROJECT_ROOT"

    create_output_dirs

    case "${1:-help}" in
        all)
            build_all
            ;;
        mac)
            build_mac
            ;;
        android)
            build_android
            ;;
        ios)
            build_ios
            ;;
        clean)
            clean_build
            ;;
        help|--help|-h)
            show_help
            ;;
        *)
            print_error "알 수 없는 옵션: $1"
            show_help
            exit 1
            ;;
    esac
}

main "$@"
