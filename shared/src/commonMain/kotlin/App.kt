import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import constants.AppConstant.SHADY
import constants.BottomNavigation
import di.RuntimeCache
import moe.tlaster.precompose.PreComposeApp
import ui.splash.AinMbatBottomNavigation
import ui.splash.Content
import util.Navigator

@Composable
fun App(
    cache: RuntimeCache = RuntimeCache,
    isMobile: Boolean = false,
    mobileContent: @Composable (
        PaddingValues,
        String,
        Navigator
    ) -> Unit = { paddingValues: PaddingValues, screen: String, _ ->
        Content(
            padding = paddingValues,
            selectedScreen = screen,
        )
    },
    activityNavigator: Navigator
) {
    PreComposeApp {
        val screens =
            if (cache.getList<String>(SHADY).isNotEmpty()) cache.getList(SHADY)
            else listOf(
                BottomNavigation.ngelaras,
                BottomNavigation.rekaman
            )
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AinMbatBottomNavigation(screens = screens) { padding, screen ->
                if (!isMobile) {
                    Content(padding = padding, selectedScreen = screen)
                } else {
                    mobileContent(padding, screen, activityNavigator)
                }
            }
        }
    }
}

expect fun getPlatformName(): String
expect fun navigateToLanscapeUI()