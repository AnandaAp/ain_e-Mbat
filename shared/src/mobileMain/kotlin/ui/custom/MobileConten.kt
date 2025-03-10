package ui.custom

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import constants.BottomNavigation
import navigation.NgelarasNav
import ui.splash.DummyDashboardUI
import util.Navigator
import viewmodel.basic.BaseViewModel
import viewmodel.basic.NgelarasViewModel

@Composable
fun MobileContent(
    padding: PaddingValues,
    selectedScreen: String,
    vararg viewModel: BaseViewModel,
    navigator: Navigator
) {
    var ngelarasViewModel: NgelarasViewModel? = null
    if (viewModel.isNotEmpty()) {
        viewModel.forEach {
            if (it is NgelarasViewModel) {
                ngelarasViewModel = it
            }
        }
    }

    AnimatedContent(selectedScreen) { screen ->
        when (screen) {
            BottomNavigation.ngelaras -> ngelarasViewModel?.let {
                NgelarasNav(
                    padding = padding,
                    selectedScreen = screen,
                    viewModel = it,
                    activityNavigator = navigator
                )
            }
            else -> DummyDashboardUI(padding = padding, selectedScreen = screen)
        }
    }
}