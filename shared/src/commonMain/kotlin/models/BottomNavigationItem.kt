package models

import constants.Characters
import ui.splash.getIconForScreen
import ui.splash.getRouteForScreen

data class BottomNavigationItem(
    val label : String = Characters.EMPTY,
    val icon : String = getIconForScreen(label),
    val route : String = getRouteForScreen(label)
)