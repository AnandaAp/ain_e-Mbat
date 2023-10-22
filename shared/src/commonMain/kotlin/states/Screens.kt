package states

sealed class Screens(val route : String) {
    object Ngelaras : Screens("screen.ngelaras")
    object Rekaman : Screens("screen.rekaman")
    object Feedback : Screens("screen.feedback")
    object Tentang : Screens("screen.tentang")
    object Pengaturan : Screens("screen.pengaturan")
}
