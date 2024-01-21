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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import constants.AppConstant
import constants.DefaultPadding
import models.CardModel
import models.CategoryOfGamelan
import states.AinAnimationState
import util.shimmerLoadingAnimation

@Composable
fun AinLazyColumn(
    padding: PaddingValues = PaddingValues(all = DefaultPadding.DEFAULT_ALL),
    item: List<CardModel>,
    animateState: AinAnimationState = AinAnimationState.Keep,
    selectedItem: (Any?) -> Unit? = {  },
    cardOnClick: (Any?) -> Unit = { selectedItem(it) },
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    columns: GridCells = GridCells.Adaptive(minSize = DefaultPadding.DEFAULT_GRID_MIN_SIZE),
    modifier: Modifier = Modifier
        .padding(padding)
        .wrapContentSize()
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement
    ) {
        LazyVerticalGrid(
            modifier = modifier,
            columns = columns,
            verticalArrangement = Arrangement.spacedBy(DefaultPadding.DEFAULT_CONTENT_VERTICAL_PADDING),
            horizontalArrangement = Arrangement.spacedBy(DefaultPadding.DEFAULT_CONTENT_HORIZONTAL_PADDING),
            contentPadding = PaddingValues(all = DefaultPadding.DEFAULT_CONTENT_PADDING_ALL)
        ) {
            if (item.isNullOrEmpty()) {
                println("animated state: $animateState")
                items(8) {
                    AinCard(modifier = Modifier
                        .fillMaxWidth()
                        .shimmerLoadingAnimation(state = animateState))
                }
            } else {
                items(items = item) { component ->
                    AinCard(
                        title = if (component.isNotNullOrEmpty()) component.title else AppConstant.DEFAULT_STRING_VALUE,
                        onClick = {
                            cardOnClick(component)
                            selectedItem(component)
                        }
                    )
                }
            }
        }
    }
}