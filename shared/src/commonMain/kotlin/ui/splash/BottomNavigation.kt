package ui.splash

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import constants.AppConstant
import constants.BottomNavigation.feedback
import constants.BottomNavigation.ngelaras
import constants.BottomNavigation.pengaturan
import constants.BottomNavigation.rekaman
import constants.BottomNavigation.tentang
import constants.Characters.EMPTY
import constants.ContentDescriptionConstant.ACCOUNT_DESCRIPTION
import constants.DefaultPadding
import constants.FilesPathConstant.ABOUT_PATH
import constants.FilesPathConstant.FEEDBACK_PATH
import constants.FilesPathConstant.NGELARAS_PATH
import constants.FilesPathConstant.REKAMAN_PATH
import constants.FilesPathConstant.SETTING_PATH
import constants.Numbers.ZERO
import di.RuntimeCache
import models.BottomNavigationItem
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import states.Screens
import ui.custom.BaseGradient
import ui.ngelaras.BaseNgelaras

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AinMbatBottomNavigation(
    screens: List<String> = listOf(),
    cache: RuntimeCache = RuntimeCache,
    content: @Composable (PaddingValues, String) -> Unit = {  padding, selectedScreen ->
        Content(
            padding = padding,
            selectedScreen = selectedScreen
        )
    }
) {
    val bottomNavItems = remember { mutableStateOf(listOf<BottomNavigationItem>()) }
    var selectedScreen by remember { mutableStateOf(screens.first()) }
    var selectedIndex by remember { mutableStateOf(ZERO) }
    var showContent by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = screens) {
        if (screens.isNotEmpty()) {
            val tempScreen = mutableStateListOf<BottomNavigationItem>()
            screens.forEach { label ->
                tempScreen.add(BottomNavigationItem(label = label))
            }
            bottomNavItems.value = tempScreen
        }
    }

    if (bottomNavItems.value.isNotEmpty()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.07f)
                .background(color = MaterialTheme.colorScheme.surface)
            ) {
                 Column(
                     modifier = Modifier
                         .padding(end = 14.dp)
                         .fillMaxSize(),
                     verticalArrangement = Arrangement.Center,
                     horizontalAlignment = Alignment.End
                 ) {
                     Icon(
                         imageVector = Icons.Default.AccountCircle,
                         contentDescription = ACCOUNT_DESCRIPTION,
                         tint = MaterialTheme.colorScheme.onSurface,
                         modifier = Modifier.size(35.dp)
                     )
                 }
            }
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    val gradient = BaseGradient()

                    BottomAppBar(modifier = Modifier.background(gradient)) {
                        bottomNavItems.value.forEachIndexed { index, screen ->
                            NavigationBarItem(
                                selected = index == selectedIndex,
                                label = { Text(text = screen.label) },
                                icon = {
                                    Icon(
                                        modifier = Modifier.size(31.dp),
                                        painter = painterResource(screen.icon),
                                        contentDescription = null
                                    )
                                },
                                onClick = {
                                    selectedIndex = index
                                    selectedScreen = screen.label
                                    showContent = !showContent
                                }
                            )
                        }
                    }
                },
                topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
                        title = {},
                        navigationIcon = {}
                    )
                },
                content = { bottomNavigationPadding -> content(bottomNavigationPadding, selectedScreen) }
            )
        }
    }
}

@Composable
fun Content(
    padding: PaddingValues,
    selectedScreen: String = AppConstant.DEFAULT_STRING_VALUE
) {
    AnimatedContent(selectedScreen) {
        when (it) {
            ngelaras -> BaseNgelaras(padding = padding, selectedScreen = it)
            else -> DummyDashboardUI(padding = padding, selectedScreen = it)
        }
    }
}

@Composable
fun DummyDashboardUI(padding: PaddingValues, selectedScreen: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Selected Screen: $selectedScreen")
    }
}

fun getIconForScreen(screen: String): String {
    return when (screen) {
        ngelaras -> NGELARAS_PATH
        rekaman -> REKAMAN_PATH
        feedback -> FEEDBACK_PATH
        tentang -> ABOUT_PATH
        pengaturan -> SETTING_PATH
        else -> EMPTY
    }
}

fun getRouteForScreen(screen: String): String {
    return when (screen) {
        ngelaras -> Screens.Ngelaras.route
        rekaman -> Screens.Rekaman.route
        feedback -> Screens.Feedback.route
        tentang -> Screens.Feedback.route
        pengaturan -> Screens.Pengaturan.route
        else -> EMPTY
    }
}