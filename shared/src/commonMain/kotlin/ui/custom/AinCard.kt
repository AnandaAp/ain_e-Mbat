package ui.custom

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import constants.AppConstant
import constants.DefaultPadding
import constants.DefaultSize
import constants.isNotNullOrEmpty

@Composable
fun AinCard(
    title: String,
    subtitle: String = AppConstant.DEFAULT_STRING_VALUE,
    additionalText: String = AppConstant.DEFAULT_STRING_VALUE,
    modifier: Modifier = Modifier.fillMaxWidth(),
    verticalArrangement: Arrangement.HorizontalOrVertical = Arrangement.Center,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    onClick: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(percent = DefaultSize.DEFAULT_ROUNDED_CORNER_PERCENTAGE),
        modifier = Modifier.clickable(enabled = true, onClick = onClick)
    ) {
        Column (
            modifier = modifier.padding(all = DefaultPadding.DEFAULT_CONTENT_PADDING_ALL),
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment
        ) {
            Text(text = title, fontWeight = FontWeight.Bold)
            if (subtitle.isNotNullOrEmpty()) {
                Text(text = subtitle)
            }
            if (additionalText.isNotNullOrEmpty()) {
                Text(text = additionalText)
            }
        }
    }
}