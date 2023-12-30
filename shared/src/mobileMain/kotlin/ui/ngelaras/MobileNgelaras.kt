package ui.ngelaras

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import constants.AppConstant
import constants.DefaultPadding
import constants.RuntimeCacheConstant
import constants.dummyGamelanList
import models.Gamelan
import states.Status
import ui.custom.NgelarasLazyColumn
import viewmodel.NgelarasViewModel

@Composable
fun MobileNgelaras(
    padding: PaddingValues = PaddingValues(all = DefaultPadding.DEFAULT_ALL),
    selectedScreen: String = AppConstant.DEFAULT_STRING_VALUE,
    viewModel: NgelarasViewModel = NgelarasViewModel()
) {
    var onClickState by remember { mutableStateOf(false) }
    var selectedGamelan by remember { mutableStateOf(Gamelan()) }
    val selectedUIState = remember { mutableStateOf(Status.Unknown) }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(
                start = DefaultPadding.DEFAULT_WIDTH,
                end = DefaultPadding.DEFAULT_WIDTH,
                bottom = DefaultPadding.DEFAULT_HEIGHT
            )
            .fillMaxSize()
    ) {
        NgelarasLazyColumn(
            padding = padding,
            item = viewModel.cache.getList<Gamelan>(RuntimeCacheConstant.GAMELAN_KEY),
            selectedScreen = selectedScreen,
            cardOnClick = { gamelan ->
                onClickState = !onClickState
                selectedGamelan = gamelan
                viewModel.computeGamelan(selectedGamelan = selectedGamelan) { status ->
                    selectedUIState.value = status
                }
            }
        )

        when (selectedUIState.value) {
            Status.Success -> println("sukses")
            Status.Failed -> println("gagal")
            Status.Unknown -> println("ga tau")
            Status.Pending -> println("pending")
            else -> println("ga tau")
        }
    }
}