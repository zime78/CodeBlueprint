import SwiftUI
import composeApp

/**
 * Compose Multiplatform UI를 감싸는 SwiftUI 뷰
 */
struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea(.keyboard) // Compose가 키보드 처리
    }
}

/**
 * UIKit의 UIViewController를 SwiftUI로 래핑
 */
struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        // 업데이트 로직 (필요 시)
    }
}
