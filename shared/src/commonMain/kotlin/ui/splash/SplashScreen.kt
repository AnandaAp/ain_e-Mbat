package ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import constants.ContentDescriptionConstant.LOGO_DESCRIPTION
import constants.FilesPathConstant.LOGO_PATH
import constants.SplashScreen
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.theme.Black80
import ui.theme.Gold40
import ui.theme.Gold60
import ui.theme.Gold80
import ui.theme.Gold90

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SplashScreen() {
    Column(
        modifier = Modifier
            .background(brush = Brush.horizontalGradient(
                colors = listOf(
                    Gold40,
                    Gold60,
                    Gold80,
                    Gold90
                )
            ))
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(LOGO_PATH),
            contentDescription = LOGO_DESCRIPTION,
            modifier = Modifier.size(200.dp),
            tint = Black80
        )
        Text(
            text = SplashScreen.DIRECTED_BY,
            color = Black80
        )
        Text(
            text = SplashScreen.AUTHOR,
            fontSize = 24.sp,
            color = Black80
        )
    }
}