package models

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
data class ShimmerAnimationData(
    private val isLightMode: Boolean
) {
    fun getColours(): List<Color> {
        return if (isLightMode) {
            val color = Color.White

            listOf(
                color.copy(alpha = 0.3f),
                color.copy(alpha = 0.5f),
                color.copy(alpha = 1.0f),
                color.copy(alpha = 0.5f),
                color.copy(alpha = 0.3f),
            )
        } else {
            val color = Color.Black

            listOf(
                color.copy(alpha = 0.0f),
                color.copy(alpha = 0.3f),
                color.copy(alpha = 0.5f),
                color.copy(alpha = 0.3f),
                color.copy(alpha = 0.0f),
            )
        }
    }
}
