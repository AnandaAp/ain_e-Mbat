package navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import constants.AppConstant
import constants.DefaultPadding
import models.NgelarasRoute
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import ui.ngelaras.MobileNgelaras
import viewmodel.NgelarasViewModel

@Composable
fun NgelarasNav(
    padding: PaddingValues = PaddingValues(all = DefaultPadding.DEFAULT_ALL),
    selectedScreen: String = AppConstant.DEFAULT_STRING_VALUE,
    ngelarasViewModel: NgelarasViewModel = NgelarasViewModel()
) {
    val navigator = rememberNavigator()

    NavHost(
        navigator = navigator,
        initialRoute = NgelarasRoute.NgelarasHome.route,
        navTransition = NavTransition()
    ) {
        scene(route = NgelarasRoute.NgelarasHome.route) {
            MobileNgelaras(
                padding = padding,
                selectedScreen = selectedScreen,
                viewModel = ngelarasViewModel
            )
        }

        scene(route = NgelarasRoute.NgelarasGamelanList.route) {

        }
    }
}