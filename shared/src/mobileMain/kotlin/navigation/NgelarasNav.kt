package navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import constants.AppConstant
import constants.DefaultPadding
import constants.NgelarasConstant
import models.NgelarasRoute
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition
import ui.ngelaras.MobileNgelaras
import ui.ngelaras.NgelarasListOfGamelan
import util.Navigator
import viewmodel.basic.NgelarasViewModel

@Composable
fun NgelarasNav(
    padding: PaddingValues = PaddingValues(all = DefaultPadding.DEFAULT_ALL),
    selectedScreen: String = AppConstant.DEFAULT_STRING_VALUE,
    viewModel: NgelarasViewModel = NgelarasViewModel(),
    activityNavigator: Navigator
) {
    val navigator = rememberNavigator()

    NavHost(
        navigator = navigator,
        initialRoute = NgelarasRoute.NgelarasHome.route,
        navTransition = NavTransition()
    ) {
        scene(route = NgelarasRoute.NgelarasHome.route) {
            val animateState by viewModel.animateState.collectAsStateWithLifecycle()
            MobileNgelaras(
                navigator = navigator,
                padding = padding,
                selectedScreen = selectedScreen,
                viewModel = viewModel,
                animateState = animateState
            )
        }

        scene(route = NgelarasRoute.NgelarasGamelanList.route) {
            val topTitle = viewModel.selectedCategoryOfGamelan.collectAsStateWithLifecycle()
            val animateState = viewModel.animateState.collectAsStateWithLifecycle()

            NgelarasListOfGamelan(
                navigator = navigator,
                activityNavigator = activityNavigator,
                padding = padding,
                fetchListOfGamelan = { viewModel.fetchListOfGamelan() },
                cardModels = viewModel.retrievedCardModel.collectAsStateWithLifecycle(),
                topTitle = topTitle.value.name,
                onCardClick = {
                    if (it != null) {
                        viewModel.computeCardOnClick(
                            selectedItem = it,
                            cachedKey = NgelarasConstant.NGELARAS_SELECTED_GAMELAN
                        )
                    }
                },
                animateState = animateState.value
            )
        }

        scene(route = NgelarasRoute.NgelarasGamelanPage.route) {
            viewModel.navigateToLandscapeNavigator(activityNavigator)
        }
    }
}