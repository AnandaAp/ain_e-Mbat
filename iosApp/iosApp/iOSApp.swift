import SwiftUI

@main
struct iOSApp: App {
    init() {
        MobileKoinKt.doinitMobileKoin()
    }

	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
