package ui.custom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import constants.AppConstant
import models.Gamelan

@Composable
fun NgelarasLazyColumn(padding: PaddingValues, item: List<Gamelan>) {
    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        items(items = item) {
            AinCard(title = if (it.isNotNullOrEmpty()) it.name else AppConstant.DEFAULT_STRING_VALUE)
        }
    }
}