import SwiftUI
import composeApp

/**
 * iOS 앱 진입점
 */
@main
struct iOSApp: App {

    init() {
        // Koin 초기화
        KoinInitKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
