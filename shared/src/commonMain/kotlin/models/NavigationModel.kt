package models

import androidx.compose.runtime.Stable

@Stable
data class NavigationModel(
    var route: String,
    val additionalInformation: Any = Any()
)
