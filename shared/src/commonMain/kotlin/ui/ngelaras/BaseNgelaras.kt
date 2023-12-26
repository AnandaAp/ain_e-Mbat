package ui.ngelaras

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import constants.AppConstant
import constants.DefaultPadding
import constants.dummyGamelanList
import ui.custom.NgelarasLazyColumn

@Composable
fun BaseNgelaras(
    padding: PaddingValues = PaddingValues(all = DefaultPadding.DEFAULT_ALL),
    selectedScreen: String = AppConstant.DEFAULT_STRING_VALUE
) {

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
            item = dummyGamelanList()
        )
    }
}