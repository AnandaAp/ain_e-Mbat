import androidx.compose.ui.window.ComposeUIViewController
import navigator.IosNavigator

actual fun getPlatformName(): String = "iOS"
actual fun navigateToLanscapeUI() {

}

fun MainViewController() = ComposeUIViewController {
    val navigator = IosNavigator()
    App(activityNavigator = navigator)
}

fun SecondViewController() = ComposeUIViewController {

}