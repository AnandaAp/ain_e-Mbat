package ui.ngelaras

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import constants.AppConstant
import constants.DefaultPadding
import models.CardModel
import models.NgelarasRoute
import moe.tlaster.precompose.lifecycle.LocalLifecycleOwner
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator
import states.AinAnimationState
import ui.custom.AinLazyColumn
import util.isNotNullOrEmpty
import util.shimmerLoadingAnimation

@Composable
fun NgelarasListOfGamelan(
    navigator: Navigator = rememberNavigator(),
    padding: PaddingValues = PaddingValues(all = DefaultPadding.DEFAULT_ALL),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    animateState: AinAnimationState = AinAnimationState.Keep,
    modifier: Modifier = Modifier
        .padding(padding)
        .fillMaxSize(),
    fetchListOfGamelan: () -> Unit = {  },
    cardModels: State<MutableList<CardModel>> = mutableStateOf(mutableListOf()),
    topTitle: String = AppConstant.DEFAULT_STRING_VALUE,
    onCardClick: (Any?) -> Unit = {}
) {
    val owner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = owner) {
        fetchListOfGamelan()
    }

    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        Text(
            text = "${AppConstant.LIST_OF_GAMELAN_TOP_TITLE}$topTitle",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            lineHeight = TextUnit(
                value = 1.25f,
                type = TextUnitType.Em
            )
        )
        AinLazyColumn(
            animateState = animateState,
            padding = PaddingValues(
                vertical = DefaultPadding.DEFAULT_CONTENT_VERTICAL_PADDING,
                horizontal = DefaultPadding.DEFAULT_CONTENT_HORIZONTAL_PADDING
            ),
            item = cardModels.value,
            cardOnClick = {
                onCardClick(it)
                navigateToNgelarasMainPage(navigator)
            }
        )
    }
}

private fun navigateToNgelarasMainPage(navigator: Navigator) {
    navigator.navigate(NgelarasRoute.NgelarasGamelanPage.route)
}