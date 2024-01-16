package model

import android.os.Parcelable
import androidx.compose.runtime.Stable
import constants.AppConstant
import kotlinx.parcelize.Parcelize

@Stable
@Parcelize
data class WavModel(
    var numberOfFrame: Int = AppConstant.DEFAULT_INTEGER_VALUE,
    var sampleRate: Int = AppConstant.DEFAULT_INTEGER_VALUE,
    var channel: Int = AppConstant.DEFAULT_INTEGER_VALUE,
    var meanMFCCValue: FloatArray = FloatArray(1),
    var buffer: Array<DoubleArray> = emptyArray(),
    var frameOffside: Int = AppConstant.DEFAULT_INTEGER_VALUE,
    var loopCounter: Int = AppConstant.DEFAULT_INTEGER_VALUE
) : Parcelable {
    fun update (
        numberOfFrame: Int = AppConstant.DEFAULT_INTEGER_VALUE,
        sampleRate: Int = AppConstant.DEFAULT_INTEGER_VALUE,
        channel: Int = AppConstant.DEFAULT_INTEGER_VALUE,
        meanMFCCValue: FloatArray = FloatArray(1),
        buffer: Array<DoubleArray> = emptyArray(),
        frameOffside: Int = AppConstant.DEFAULT_INTEGER_VALUE,
        loopCounter: Int = AppConstant.DEFAULT_INTEGER_VALUE
    ) {
        if (numberOfFrame > 0) {
            this.numberOfFrame = numberOfFrame
        }
        if (sampleRate > 0) {
            this.sampleRate = sampleRate
        }
        if (channel > 0) {
            this.channel = channel
        }
        if (meanMFCCValue.isNotEmpty()) {
            this.meanMFCCValue = meanMFCCValue
        }
        if (buffer.isNotEmpty()) {
            this.buffer = buffer
        }
        if (frameOffside > 0) {
            this.frameOffside = frameOffside
        }
        if (loopCounter > 0) {
            this.loopCounter = loopCounter
        }
    }

    fun update(wavModel: WavModel = WavModel()) {
        update(
            numberOfFrame = wavModel.numberOfFrame,
            sampleRate = wavModel.sampleRate,
            channel = wavModel.channel,
            meanMFCCValue = wavModel.meanMFCCValue,
            buffer = wavModel.buffer,
            frameOffside = wavModel.frameOffside,
            loopCounter = wavModel.loopCounter
        )
    }

    override fun hashCode(): Int {
        var result = numberOfFrame
        result = 31 * result + sampleRate
        result = 31 * result + channel
        result = 31 * result + meanMFCCValue.contentHashCode()
        result = 31 * result + buffer.contentDeepHashCode()
        result = 31 * result + frameOffside
        result = 31 * result + loopCounter
        return result
    }
}
