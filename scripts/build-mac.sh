#!/bin/bash
# Mac 앱 빌드 (DMG)

set -e
cd "$(dirname "${BASH_SOURCE[0]}")/.."

echo "🖥️  Mac 앱 빌드 시작..."

# DMG 패키지 빌드
./gradlew :desktopApp:packageDmg --no-daemon

echo ""
echo "✅ 빌드 완료!"
echo "📁 출력 위치: desktopApp/build/compose/binaries/main/dmg/"
ls -la desktopApp/build/compose/binaries/main/dmg/*.dmg 2>/dev/null || echo "⚠️  DMG 파일을 찾을 수 없습니다. 아이콘 파일이 필요할 수 있습니다."
