package ui.ngelaras

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import constants.AppConstant
import constants.DefaultPadding
import constants.ResourceDefault
import models.Gamelan
import ui.custom.AinRecordButton
import util.isNotNullOrEmpty
import util.toOneDigitAfterDecimalPoint

@Composable
fun BaseLandscapeNgelaras(
    modifier: Modifier = Modifier.fillMaxSize(),
    pitch: String = AppConstant.DEFAULT_STRING_VALUE,
    hertz: Float = AppConstant.DEFAULT_FLOAT_VALUE,
    model: Gamelan = Gamelan(),
    onRecordButtonClick: () -> Unit = {},
    buttonState: Boolean = AppConstant.DEFAULT_BOOLEAN_VALUE
) {
    Surface {
        Column(
            modifier = modifier.padding(DefaultPadding.DEFAULT_ALL),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = ResourceDefault.DEFAULT_RECORD_TITLE,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                modifier = Modifier.fillMaxWidth()
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(weight = .5f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (buttonState && pitch.isNotNullOrEmpty()) {
                    Text (
                        text = "Kunci nada: $pitch",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (buttonState && hertz > -1.0f) {
                    Text (
                        text = "Frequency nada: ${hertz.toOneDigitAfterDecimalPoint()}",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(weight = .5f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AinRecordButton(
                    onClick = onRecordButtonClick,
//                    modifier = Modifier
//                        .width(width = 41.dp)
//                        .height(height = 82.dp)
                )
            }
        }
    }
}