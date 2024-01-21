package models

import constants.AppConstant

sealed class NgelarasRoute (val route: String = AppConstant.DEFAULT_STRING_VALUE) {
    protected fun navigateWithArgs(uuid: Long): String {
        return this.route.replace("{uuid}", "$uuid")
    }

    data object NgelarasHome: NgelarasRoute(route = "/ngelaras/home/{uuid}")
    data object NgelarasGamelanList: NgelarasRoute(route = "/ngelaras/gamelan/{uuid}")
    data object NgelarasGamelanPage: NgelarasRoute(route = "/ngelaras/main/{uuid}")
}