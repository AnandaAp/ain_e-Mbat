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
import ui.custom.AinRecordButton
import util.isNotNullOrEmpty

@Composable
fun BaseLandscapeNgelaras(
    modifier: Modifier = Modifier.fillMaxSize(),
    pitch: String = AppConstant.DEFAULT_STRING_VALUE,
    onRecordButtonClick: () -> Unit = {}
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
            if (pitch.isNotNullOrEmpty()) {
                Text (
                    text = "Kunci nada: $pitch",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AinRecordButton(onClick = onRecordButtonClick)
            }
        }
    }
}