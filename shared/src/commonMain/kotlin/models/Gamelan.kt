package models

import androidx.compose.runtime.Stable
import constants.AppConstant
import kotlinx.serialization.Serializable

@Stable
@Serializable
/**
 *  Model class for Gamelan object. This class contain the name of the Gamelan.
 */
data class Gamelan(
    var name: String =  AppConstant.DEFAULT_STRING_VALUE,
    var unique: String = AppConstant.DEFAULT_STRING_VALUE,
    val frequency: GamelanFrequency = GamelanFrequency()
) {
    override fun toString() = "Gamelan {\n" +
            "\tname: $name\n" +
            "\tfrequency: $frequency\n" +
            "}"

    fun isNotNullOrEmpty() = this != null && isNotEmpty()

    fun isNotEmpty() = !isEmpty()

    fun isEmpty() = name.isEmpty() && unique.isEmpty()

    companion object DummyGamelan {
        val gamelan = Gamelan(name = AppConstant.DEFAULT_GAMELAN_VALUE)
    }
}