package ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun AinMbatBottomNavigation(
    screens: List<String> = listOf(),
) {
    var selectedScreen by remember { mutableStateOf(screens.first()) }
    var showContent by remember { mutableStateOf(true) }

    Scaffold(
        bottomBar = {
            BottomNavigation {
                screens.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                imageVector = getIconForScreen(screen),
                                contentDescription = screen
                            )
                        },
                        label = { Text(screen) },
                        selected = screen == selectedScreen,
                        onClick = {
                            selectedScreen = screen
                            showContent = !showContent
                        },
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        },
        content = { bottomNavigationPadding ->
            AnimatedContent(selectedScreen) {
                Column(
                    modifier = Modifier.padding(bottomNavigationPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Selected Screen: $selectedScreen")
                }
            }
        }
    )
}

@Composable
fun getIconForScreen(screen: String): ImageVector {
    return when (screen) {
        "Home" -> Icons.Default.Home
        "Feed" -> Icons.Default.AccountBox
        "Post" -> Icons.Default.Add
        "Alert" -> Icons.Default.Notifications
        "Jobs" -> Icons.Default.Done
        else -> Icons.Default.Home
    }
}