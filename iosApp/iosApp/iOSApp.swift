import SwiftUI


@main
struct iOSApp: App {

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

//
//
//
// import SwiftUI
// import shared
//
// @main
// struct iOSApp: App {
//     var body: some Scene {
//         WindowGroup {
//             MainViewControllerWrapper()
//         }
//     }
// }
//
// struct MainViewControllerWrapper: UIViewControllerRepresentable {
//     func makeUIViewController(context: Context) -> UIViewController {
//         MainViewController()
//     }
//
//     func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
// }
// import SwiftUI
// import shared
//
// @main
// struct iOSApp: App {
//     var body: some Scene {
//         WindowGroup {
//             ComposeView()
//                 .ignoresSafeArea(.keyboard)
//         }
//     }
// }
//
// struct ComposeView: UIViewControllerRepresentable {
//     func makeUIViewController(context: Context) -> UIViewController {
//         MainViewControllerKt.MainViewController()
//     }
//
//     func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
// }
