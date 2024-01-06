import androidx.compose.ui.window.ComposeUIViewController

actual fun getPlatformName(): String = "iOS"
actual fun navigateToLanscapeUI() {

}

fun MainViewController() = ComposeUIViewController {
    App()
}