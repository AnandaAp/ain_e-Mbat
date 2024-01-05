package models

import androidx.compose.runtime.Stable
import constants.AppConstant
import kotlinx.serialization.Serializable

@Stable
@Serializable
/**
 *  Model class for The category of Gamelan object.
 */
data class CategoryOfGamelan(
    var name: String = AppConstant.DEFAULT_STRING_VALUE,
    val unique: String = AppConstant.DEFAULT_STRING_VALUE
) {
    override fun toString() = "Category of Gamelan {\n" +
            "\tname: $name\n" +
            "\tunique: $unique\n" +
            "}"

    fun isNotNullOrEmpty() = this != null && isNotEmpty()

    fun isNotEmpty() = !isEmpty()

    fun isEmpty() = name.isEmpty() && unique.isEmpty()

    companion object DummyCategoryOfGamelan {
        val category = CategoryOfGamelan(name = AppConstant.DEFAULT_GAMELAN_VALUE)
    }
}