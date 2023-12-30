import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import constants.AppConstant.SHADY
import constants.BottomNavigation
import di.RuntimeCache
import moe.tlaster.precompose.PreComposeApp
import ui.splash.AinMbatBottomNavigation
import ui.splash.Content

@Composable
fun App(
    cache: RuntimeCache = RuntimeCache,
    isMobile: Boolean = false,
    mobileContent: @Composable (
        PaddingValues,
        String
    ) -> Unit = { paddingValues: PaddingValues, screen: String ->
        Content(
            padding = paddingValues,
            selectedScreen = screen
        )
    }
) {
    PreComposeApp {
        MaterialTheme {
            val screens =
                if (cache.getList<String>(SHADY).isNotEmpty()) cache.getList(SHADY)
                else listOf(
                    BottomNavigation.ngelaras,
                    BottomNavigation.rekaman
                )
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AinMbatBottomNavigation(screens = screens) { padding, screen ->
                    if (!isMobile) {
                        Content(padding = padding, selectedScreen = screen)
                    } else {
                        mobileContent(padding, screen)
                    }
                }
            }
        }
    }
}

expect fun getPlatformName(): String