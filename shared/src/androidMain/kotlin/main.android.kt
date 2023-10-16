import androidx.compose.runtime.Composable
import di.RuntimeCache

actual fun getPlatformName(): String = "Android"

@Composable fun MainView(cache: RuntimeCache = RuntimeCache()) = App(cache)
