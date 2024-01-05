package ui.ngelaras

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import constants.AppConstant
import constants.DefaultPadding
import constants.NgelarasConstant
import constants.RuntimeCacheConstant
import models.CategoryOfGamelan
import models.NgelarasRoute
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator
import states.AinAnimationState
import states.Status
import ui.custom.AinStatusHandler
import ui.custom.NgelarasLazyColumn
import ui.custom.ShimmerLoadingScreen
import viewmodel.NgelarasViewModel

@Composable
fun MobileNgelaras(
    navigator: Navigator = rememberNavigator(),
    animateState: AinAnimationState = AinAnimationState.Keep,
    padding: PaddingValues = PaddingValues(all = DefaultPadding.DEFAULT_ALL),
    selectedScreen: String = AppConstant.DEFAULT_STRING_VALUE,
    viewModel: NgelarasViewModel = NgelarasViewModel(),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    modifier: Modifier = Modifier
        .padding(padding)
        .fillMaxSize()
) {
    var onClickState by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(CategoryOfGamelan()) }
    val selectedUIState = remember { mutableStateOf(Status.Unknown) }

    LaunchedEffect(key1 = animateState) {
        viewModel.checkAnimatedState(key = RuntimeCacheConstant.CATEGORY_OF_GAMELAN_KEY)
    }

    when (animateState) {
        AinAnimationState.Keep -> ShimmerLoadingScreen(state = animateState, padding = padding)
        else -> Column(
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            modifier = modifier
        ) {
            NgelarasLazyColumn(
                padding = padding,
                item = viewModel.cache.getList(RuntimeCacheConstant.CATEGORY_OF_GAMELAN_KEY),
                selectedScreen = selectedScreen,
                cardOnClick = { gamelan ->
                    onClickState = !onClickState
                    selectedCategory = gamelan
                    viewModel.computeCardOnClick(
                        selectedItem = selectedCategory,
                        cachedKey = NgelarasConstant.NGELARAS_SELECTED_CATEGORY_GAMELAN
                    ) { status ->
                        selectedUIState.value = status
                    }
                }
            )

            AinStatusHandler(status = selectedUIState.value, onSuccessCallBack = { NavigateToListOfGamelan(navigator) })
        }
    }
}

@Composable
private fun NavigateToListOfGamelan(navigator: Navigator) {
    navigator.navigate(route = NgelarasRoute.NgelarasGamelanList.route)
}