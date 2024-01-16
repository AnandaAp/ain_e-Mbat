package util

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioRecord
import android.os.Build
import androidx.annotation.RequiresApi
import model.AudioModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber
import kotlin.math.ln


@SuppressLint("MissingPermission")
class AudioProcessor(val audioModel: AudioModel): KoinComponent {
    private val context: Context by inject()

    val option = AudioRecord
        .Builder()
        .setAudioSource(audioModel.audioSource)
        .setAudioFormat(audioModel.audioFormat)
        .setBufferSizeInBytes(audioModel.bufferSize)

    private var recorder = option.build()
    private val yin = Yin(recorder.sampleRate.toFloat())
    var pitch: Float = 0F

    /**
     * Start real time annotation.
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun startRecording() {
        if (recorder.state == AudioRecord.STATE_UNINITIALIZED) {
            recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                option.setContext(context).build()
            } else {
                option.build()
            }
        }

        if (recorder.state == AudioRecord.STATE_INITIALIZED) {
            yin.isRunning = true
            recorder.startRecording()
            yin.processStream(recorder, Yin.printDetectPitchHandler)
        }
    }

    /**
     * Stops real time annotation.
     */
    fun stopRecording() {
        if (yin.isRunning && yin.instance!!.isRunning) {
            yin.isRunning = false
            yin.instance!!.isRunning = false
            recorder.stop()
        }
    }

    fun release() {
        if (recorder.state == AudioRecord.STATE_INITIALIZED) {
            recorder.release()
        }
    }

    fun proceedAudio(readBufferSize: Int) {
        var counter = 0
        while (recorder.recordingState != AudioRecord.RECORDSTATE_STOPPED) {
            val shortArray = ShortArray(readBufferSize / 2)
            recorder.read(shortArray, 0, readBufferSize / 2)
            if (counter == 0) {
                val instantaneousPitch = yin.getPitch(ShortArray(readBufferSize / 2))
                if (instantaneousPitch >= 0) {
                    pitch = ((2.0 * pitch + instantaneousPitch) / 3.0).toFloat()
                    printPitch()
                }
            }
            counter = (counter + 1) % 5
        }
    }
    fun log(x: Double, base: Double): Double = (ln(x) / ln(base))
    fun printPitch() {
        Timber.tag("AinAudio").e("pitch: ${pitch}hz")
    }
}