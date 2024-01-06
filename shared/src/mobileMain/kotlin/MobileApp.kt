import androidx.compose.runtime.Composable
import di.RuntimeCache
import ui.custom.MobileContent
import util.Navigator
import viewmodel.NgelarasViewModel

@Composable
fun MobileApp(
    cache: RuntimeCache = RuntimeCache,
    isMobile: Boolean = true,
    ngelarasViewModel: NgelarasViewModel,
    navigator: Navigator
) {
    App(
        cache = cache,
        isMobile,
        onNewActivity = navigator,
        mobileContent = { paddingValues, screen, onNavigator ->
            MobileContent(
                padding = paddingValues,
                selectedScreen = screen,
                viewModel = arrayOf(ngelarasViewModel),
                navigator = onNavigator
            )
        }
    )
}