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
import ui.custom.NgelarasLazyColumn
import util.dummyCategoryOfGamelan

@Composable
fun BaseNgelaras(
    padding: PaddingValues = PaddingValues(all = DefaultPadding.DEFAULT_ALL),
    selectedScreen: String = AppConstant.DEFAULT_STRING_VALUE
) {
    var onClickState by remember { mutableStateOf(false) }

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
            item = dummyCategoryOfGamelan(),
            selectedScreen = selectedScreen,
            cardOnClick = { onClickState = !onClickState }
        )
    }
}