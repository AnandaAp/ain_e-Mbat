package util

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import constants.AppConstant
import models.CategoryOfGamelan
import models.Gamelan
import models.ShimmerAnimationData
import moe.tlaster.precompose.stateholder.SavedStateHolder
import states.AinAnimationState
import kotlin.math.round

fun String.isNotNullOrEmpty() = this != null
        && this.isNotBlank()
        && this.isNotEmpty()
        && this != AppConstant.DEFAULT_STRING_VALUE

fun List<*>.isNotNullOrEmpty(): Boolean = this != null && this.isNotEmpty()

fun dummyGamelanList() = listOf(Gamelan.gamelan, Gamelan.gamelan, Gamelan.gamelan)

fun dummyCategoryOfGamelan() = listOf(
    CategoryOfGamelan.category,
    CategoryOfGamelan.category,
    CategoryOfGamelan.category
)

@Composable
private fun Modifier.shimmerLoadingAnimationLogic(
    widthOfShadowBrush: Int,
    angleOfAxisY: Float,
    durationMillis: Int,
    isLightModeActive: Boolean
): Modifier {
    return composed {
        val shimmerColors = ShimmerAnimationData(isLightMode = isLightModeActive).getColours()

        val transition = rememberInfiniteTransition(label = AppConstant.DEFAULT_STRING_VALUE)

        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = durationMillis,
                    easing = LinearEasing,
                ),
                repeatMode = RepeatMode.Restart,
            ),
            label = "Ain Shimmer loading animation",
        )

        this.background(
            brush = Brush.linearGradient(
                colors = shimmerColors,
                start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
                end = Offset(x = translateAnimation.value, y = angleOfAxisY),
            )
        )
    }
}

fun Modifier.shimmerLoadingAnimation(
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
    state: AinAnimationState = AinAnimationState.Hide,
    isLightModeActive: Boolean = true,
): Modifier {
    return composed {
        fillMaxWidth()
        when (state) {
            AinAnimationState.Keep -> {
                shimmerLoadingAnimationLogic(
                    widthOfShadowBrush,
                    angleOfAxisY,
                    durationMillis,
                    isLightModeActive
                )
            }
            AinAnimationState.KeepAndSkip -> shimmerLoadingAnimationLogic(
                widthOfShadowBrush,
                angleOfAxisY,
                durationMillis,
                isLightModeActive
            )
            else -> this
        }
    }
}

expect interface Navigator {
    fun navigate(tag: String)
}

fun SavedStateHolder.isNull(): Boolean = this == null
fun SavedStateHolder.isNotNull(): Boolean = !this.isNull()
inline fun Float.rangeOf(minimumValue: Float, maximumValue: Float) = (minimumValue..maximumValue).contains(this)
fun getMinusTolerateHertz(hertz: Float) = hertz - (hertz * 0.5f/100)
fun getPlusTolerateHertz(hertz: Float) = hertz + (hertz * 0.5f/100)
inline fun <T> getValueBasedFromCondition(condition: Boolean, trueValue: T, falseValue: T): T = if (condition) trueValue else falseValue
inline fun Float.toOneDigitAfterDecimalPoint() = round(this * 10.0f) / 10.0f