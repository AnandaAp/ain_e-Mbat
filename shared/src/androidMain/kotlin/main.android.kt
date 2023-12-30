import androidx.compose.runtime.Composable
import di.RuntimeCache
import viewmodel.NgelarasViewModel

actual fun getPlatformName(): String = "Android"

@Composable fun MainView(
    cache: RuntimeCache = RuntimeCache,
    ngelarasViewModel: NgelarasViewModel = NgelarasViewModel()
) = MobileApp(
    cache = cache,
    ngelarasViewModel = ngelarasViewModel
)
