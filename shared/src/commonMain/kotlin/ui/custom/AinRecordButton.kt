package ui.custom

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import constants.ResourceDefault

@Composable
fun AinRecordButton(
    onClick: () -> Unit = { },
    shape: Shape = CircleShape,
    modifier: Modifier = Modifier.fillMaxSize(.2f),
    enabled: Boolean = true
) {
    var onButtonClick by remember { mutableStateOf(true) }
    val icon = if (onButtonClick) Icons.Filled.Mic else Icons.Filled.MicOff
    FilledIconButton(
        onClick = {
            onClick()
            onButtonClick = !onButtonClick
        },
        modifier = modifier,
        shape = shape,
        enabled = enabled
    ) {
        Icon(
            imageVector = icon,
            contentDescription = ResourceDefault.DEFAULT_RECORD_ICON_CONTENT_DESCRIPTION,
            modifier = Modifier.fillMaxSize(.8f)
        )
    }
}