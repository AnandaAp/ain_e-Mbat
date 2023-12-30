package ui.custom

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import constants.BottomNavigation
import ui.ngelaras.MobileNgelaras
import ui.splash.DummyDashboardUI
import viewmodel.BaseViewModel
import viewmodel.NgelarasViewModel

@Composable
fun MobileContent(
    padding: PaddingValues,
    selectedScreen: String,
    vararg viewModel: BaseViewModel
) {
    var ngelarasViewModel = NgelarasViewModel()
    if (viewModel.isNotEmpty()) {
        viewModel.forEach {
            if (it is NgelarasViewModel) {
                ngelarasViewModel = it
            }
        }
    }
    AnimatedContent(selectedScreen) {
        when (it) {
            BottomNavigation.ngelaras -> MobileNgelaras(
                padding = padding,
                selectedScreen = selectedScreen,
                viewModel = ngelarasViewModel
            )
            else -> DummyDashboardUI(padding = padding, selectedScreen = it)
        }
    }
}