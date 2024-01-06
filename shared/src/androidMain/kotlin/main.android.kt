import androidx.compose.runtime.Composable
import di.RuntimeCache
import util.Navigator
import viewmodel.basic.NgelarasViewModel

actual fun getPlatformName(): String = "Android"
actual fun navigateToLanscapeUI() {}

@Composable fun MainView(
    cache: RuntimeCache = RuntimeCache,
    ngelarasViewModel: NgelarasViewModel = NgelarasViewModel(),
    navigator: Navigator
) = MobileApp(
    cache = cache,
    ngelarasViewModel = ngelarasViewModel,
    navigator = navigator
)
