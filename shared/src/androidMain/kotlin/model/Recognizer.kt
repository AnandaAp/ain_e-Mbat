package model

import android.os.Parcelable
import androidx.compose.runtime.Stable
import constants.AppConstant
import kotlinx.parcelize.Parcelize

@Stable
@Parcelize
data class Recognizer(
    val id: String = AppConstant.DEFAULT_STRING_VALUE,
    val title: String = AppConstant.DEFAULT_STRING_VALUE,
    val confidence: Float = AppConstant.DEFAULT_FLOAT_VALUE
) : Parcelable
