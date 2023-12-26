package models

import androidx.compose.runtime.Stable
import constants.AppConstant
import constants.isNotNullOrEmpty
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class Gamelan(
    var name: String
) {
    override fun toString() = "Gamelan {\n" +
            "\tname: $name\n" +
            "}"

    fun isNotNullOrEmpty() = this != null && name.isNotNullOrEmpty()

    companion object DummyGamelan {
        val gamelan = Gamelan(name = AppConstant.DEFAULT_GAMELAN_VALUE)
    }
}