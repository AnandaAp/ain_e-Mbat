package states

sealed class BottomBar(
    val icon: Int,
    val position: Int,
    val name: String = ""
) {
    object Ngelaras: BottomBar(-1, 0, "Ngelaras")
    object Rekaman: BottomBar(-1, 1, "Rekaman")
}
