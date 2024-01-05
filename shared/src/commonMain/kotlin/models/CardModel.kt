package models

import androidx.compose.runtime.Stable
import constants.AppConstant
import kotlinx.serialization.Serializable
import util.isNotNullOrEmpty

@Stable
@Serializable
/**
 *  Model class for The category of card object object.
 */
data class CardModel(
    var title: String = AppConstant.DEFAULT_STRING_VALUE,
    var subTitle: String = AppConstant.DEFAULT_STRING_VALUE,
    var additionalText: String = AppConstant.DEFAULT_STRING_VALUE
) {
    override fun toString() = "Card Model {\n" +
            "\tname: $title\n" +
            "\tsubtitle: $subTitle\n" +
            "\tadditional text: $additionalText\n" +
            "}"

    fun isNotNullOrEmpty() = this != null && isNotEmpty()

    fun isNotEmpty() = !isEmpty()

    fun isEmpty() = title.isEmpty()

    fun modify(
        newValue: CardModel = CardModel(),
        newTitle: String = AppConstant.DEFAULT_STRING_VALUE,
        newSubTitle: String = AppConstant.DEFAULT_STRING_VALUE,
        newAdditionalText: String = AppConstant.DEFAULT_STRING_VALUE
    ) {
        if (newValue.isNotNullOrEmpty()) {
            title = newValue.title
            subTitle = newValue.subTitle
            additionalText = newValue.additionalText
        }
        if (newTitle.isNotNullOrEmpty()) {
            title = newTitle
        }
        if (newSubTitle.isNotNullOrEmpty()) {
            subTitle = newSubTitle
        }
        if (newAdditionalText.isNotNullOrEmpty()) {
            additionalText = newAdditionalText
        }
    }
}