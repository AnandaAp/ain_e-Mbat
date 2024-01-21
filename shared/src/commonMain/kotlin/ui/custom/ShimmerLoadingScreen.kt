package ui.custom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import constants.DefaultPadding
import constants.DefaultSize
import states.AinAnimationState
import util.shimmerLoadingAnimation

@Composable
fun ShimmerLoadingScreen(
    state: AinAnimationState = AinAnimationState.Keep,
    padding: PaddingValues = PaddingValues(all = DefaultPadding.DEFAULT_CONTENT_PADDING_ALL),
    modifier: Modifier = Modifier
        .fillMaxSize()
        .padding(padding)
) {
    LazyColumn (
        modifier = Modifier.wrapContentSize().padding(padding),
        verticalArrangement = Arrangement.spacedBy(DefaultPadding.DEFAULT_CONTENT_VERTICAL_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(all = DefaultPadding.DEFAULT_CONTENT_PADDING_ALL)
    ) {
        item {
            ComponentRectangle(state = state)
            Spacer(modifier = Modifier.padding(top = 8.dp))
        }
        items (5) {
            ComponentRectangleLineLong(state = state)
        }
    }
}

@Composable
fun ComponentSquare(state: AinAnimationState) {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(24.dp))
            .size(100.dp)
            .shimmerLoadingAnimation(state = state)
    )
}

@Composable
fun ComponentRectangle(state: AinAnimationState) {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(24.dp))
            .height(200.dp)
            .fillMaxWidth()
            .shimmerLoadingAnimation(state = state)
    )
}

@Composable
fun ComponentRectangleLineLong(state: AinAnimationState) {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .height(30.dp)
            .shimmerLoadingAnimation(state = state)
    )
}

@Composable
fun ComponentRectangleLineShort(state: AinAnimationState) {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(DefaultSize.DEFAULT_ROUNDED_CORNER_PERCENTAGE))
            .size(height = 30.dp, width = 100.dp)
            .shimmerLoadingAnimation(state = state)
    )
}