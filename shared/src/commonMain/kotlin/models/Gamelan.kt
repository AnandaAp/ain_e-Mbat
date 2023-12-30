package models

import androidx.compose.runtime.Stable
import constants.AppConstant
import constants.isNotNullOrEmpty
import kotlinx.serialization.Serializable

@Stable
@Serializable
/**
 *  Model class for Gamelan object. This class contain the name of the Gamelan.
 */
data class Gamelan(
    var name: String =  AppConstant.DEFAULT_STRING_VALUE
) {
    override fun toString() = "Gamelan {\n" +
            "\tname: $name\n" +
            "}"

    fun isNotNullOrEmpty() = this != null && name.isNotNullOrEmpty()

    fun isNotEmpty() = name.isNotEmpty()

    fun isEmpty() = name.isEmpty()

    companion object DummyGamelan {
        val gamelan = Gamelan(name = AppConstant.DEFAULT_GAMELAN_VALUE)
    }
}