package ui.custom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import constants.AppConstant
import constants.isNotNullOrEmpty

@Composable
fun AinCard(
    title: String,
    subtitle: String = AppConstant.DEFAULT_STRING_VALUE,
    additionalText: String = AppConstant.DEFAULT_STRING_VALUE,
    modifier: Modifier = Modifier.wrapContentSize(),
    verticalArrangement: Arrangement.HorizontalOrVertical = Arrangement.Center,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally
) {
    Card(
        shape = RoundedCornerShape(percent = 25)
    ) {
        Column (
            modifier = modifier,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment
        ) {
            Text(text = title)
            if (subtitle.isNotNullOrEmpty()) {
                Text(text = subtitle)
            }
            if (additionalText.isNotNullOrEmpty()) {
                Text(text = subtitle)
            }
        }
    }
}