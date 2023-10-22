import androidx.compose.material.MaterialTheme
import androidx.compose.ui.window.ComposeUIViewController
import ui.theme.Material3AinEmbatTheme

actual fun getPlatformName(): String = "iOS"

fun MainViewController() = ComposeUIViewController {
    Material3AinEmbatTheme {
        App()
    }
}