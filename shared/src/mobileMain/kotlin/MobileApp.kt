import androidx.compose.runtime.Composable
import di.RuntimeCache
import ui.custom.MobileContent
import viewmodel.NgelarasViewModel

@Composable
fun MobileApp(cache: RuntimeCache = RuntimeCache, isMobile: Boolean = true, ngelarasViewModel: NgelarasViewModel) {
    App(cache = cache, isMobile, mobileContent = { paddingValues, screen ->
        MobileContent(padding = paddingValues, selectedScreen = screen, viewModel = arrayOf(ngelarasViewModel))
    })
}