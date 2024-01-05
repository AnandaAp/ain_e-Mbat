package ui.custom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import constants.AppConstant
import constants.DefaultPadding
import models.CategoryOfGamelan
import models.Gamelan

@Composable
fun NgelarasLazyColumn(
    padding: PaddingValues,
    item: List<CategoryOfGamelan>,
    selectedScreen: String = AppConstant.DEFAULT_STRING_VALUE,
    selectedItem: (CategoryOfGamelan) -> Unit = { CategoryOfGamelan() },
    cardOnClick: (CategoryOfGamelan) -> Unit = { selectedItem(it) },
    modifier: Modifier = Modifier
        .padding(padding)
        .fillMaxSize()
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = AppConstant.DAFTAR_GAMELAN_TITLE.uppercase(),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize
        )
        LazyVerticalGrid(
            modifier = Modifier.wrapContentSize(),
            columns = GridCells.Adaptive(minSize = DefaultPadding.DEFAULT_GRID_MIN_SIZE),
            verticalArrangement = Arrangement.spacedBy(DefaultPadding.DEFAULT_CONTENT_VERTICAL_PADDING),
            horizontalArrangement = Arrangement.spacedBy(DefaultPadding.DEFAULT_CONTENT_HORIZONTAL_PADDING),
            contentPadding = PaddingValues(all = DefaultPadding.DEFAULT_CONTENT_PADDING_ALL)
        ) {
            items(items = item) { gamelan ->
                AinCard(
                    title = if (gamelan.isNotNullOrEmpty()) gamelan.name else AppConstant.DEFAULT_STRING_VALUE,
                    onClick = {
                        cardOnClick(gamelan)
                        selectedItem(gamelan)
                    }
                )
            }
        }
    }
}