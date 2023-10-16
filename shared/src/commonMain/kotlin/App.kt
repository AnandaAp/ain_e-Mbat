import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import constants.AppConstant.SHADY
import constants.BottomNavigation
import di.RuntimeCache
import ui.splash.AinMbatBottomNavigation

@Composable
fun App(cache: RuntimeCache = RuntimeCache()) {
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
            AinMbatBottomNavigation(screens = screens)
        }
    }
}

expect fun getPlatformName(): String