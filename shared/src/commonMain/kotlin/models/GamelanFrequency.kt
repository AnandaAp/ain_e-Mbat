package models

import androidx.compose.runtime.Stable
import constants.AppConstant
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class GamelanFrequency(
    val laras: Map<String, String> = mapOf(),
    val titilaras: String = AppConstant.DEFAULT_STRING_VALUE
)