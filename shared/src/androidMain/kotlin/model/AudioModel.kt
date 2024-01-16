package model

import android.media.AudioFormat
import androidx.compose.runtime.Immutable

@Immutable
data class AudioModel(
    val audioSource: Int,
    val sampleRate: Int,
    val channelMask: Int,
    val bufferSize: Int,
    val encoding: Int,
) {
    val buffer: ShortArray = ShortArray(bufferSize)
    var audioFormat: AudioFormat = AudioFormat
        .Builder()
        .setEncoding(encoding)
        .setChannelMask(channelMask)
        .setSampleRate(sampleRate)
        .build()

    override fun hashCode(): Int {
        var result = audioSource
        result = 31 * result + sampleRate
        result = 31 * result + channelMask
        result = 31 * result + bufferSize
        result = 31 * result + buffer.contentHashCode()
        result = 31 * result + encoding
        result = 31 * result + audioFormat.hashCode()
        return result
    }
}
