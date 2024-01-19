package util

import android.media.AudioFormat
import android.media.AudioRecord
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import org.koin.core.component.KoinComponent
import timber.log.Timber
import java.io.IOException

/**
 * @author Joren Six
 * An implementation of the YIN pitch tracking algorithm.
 * See [the YIN paper.](http://recherche.ircam.fr/equipes/pcm/cheveign/ps/2002_JASA_YIN_proof.pdf)
 *
 * Implementation originally based on [aubio](http://aubio.org)
 *
 *
 * Updated by Emlyn O'Regan to work in the PitchDetect sample project for Android.
 * I removed all the realtime features (which are tied in with javax libraries, not good for Dalvik), and
 * modified Yin to be called with a byte buffer to be analyzed using the getPitch() method. So
 * just create yourself a Yin, then call getPitch(bytes) when you're ready.
 *
 * Also converted it to use an array of Shorts instead of Floats.
 *
// * Original implementation is here: http://tarsos.0110.be/artikels/lees/YIN_Pitch_Tracker_in_JAVA
 */
class Yin(private val sampleRate: Float): KoinComponent {
    //thread
    private val coroutine: CoroutineScope = CoroutineScope(Dispatchers.IO) + CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
    }
    private var bufferSize = 1024
    private var overlapSize = bufferSize / 2
    var instance: Yin? = null
    var isRunning = false

    /**
     * The YIN threshold value (see paper)
     */
    private val threshold = 0.15

    /**
     * A boolean to start and stop the algorithm.
     * Practical for real time processing of data.
     */
    //	private volatile boolean running;
    /**
     * The original input buffer
     */
    private var inputBuffer: ShortArray = ShortArray(bufferSize)

    /**
     * The buffer that stores the calculated values.
     * It is exactly half the size of the input buffer.
     */
    private lateinit var buffer: FloatArray

    /**
     * Implements the difference function as described
     * in step 2 of the YIN paper
     */
    private fun difference() {
        var innerIndex: Int
        var delta: Float
        var index = 0
        while (index < buffer.size) {
            buffer[index] = 0f
            index++
        }
        index = 1
        while (index < buffer.size) {
            innerIndex = 0
            while (innerIndex < buffer.size) {
                delta = inputBuffer[innerIndex].toFloat() - inputBuffer[innerIndex + index].toFloat()
                buffer[index] += delta * delta
                innerIndex++
            }
            index++
        }
    }

    /**
     * The cumulative mean normalized difference function
     * as described in step 3 of the YIN paper
     * <br></br>`
     * yinBuffer[0] == yinBuffer[1] = 1
    ` *
     *
     */
    private fun cumulativeMeanNormalizedDifference() {
        buffer[0] = 1f
        //Very small optimization in comparison with AUBIO
        //start the running sum with the correct value:
        //the first value of the yinBuffer
        var runningSum = buffer[1]
        //yinBuffer[1] is always 1
        buffer[1] = 1f
        //now start at tau = 2
        var tau = 2
        while (tau < buffer.size) {
            runningSum += buffer[tau]
            buffer[tau] *= tau / runningSum
            tau++
        }
    }

    /**
     * Implements step 4 of the YIN paper
     */
    private fun absoluteThreshold(): Int {
        //Uses another loop construct
        //than the AUBIO implementation
        var tau = 1
        while (tau < buffer.size) {
            if (buffer[tau] < threshold) {
                while (tau + 1 < buffer.size &&
                    buffer[tau + 1] < buffer[tau]
                ) tau++
                return tau
            }
            tau++
        }
        //no pitch found
        return -1
    }

    /**
     * Implements step 5 of the YIN paper. It refines the estimated tau value
     * using parabolic interpolation. This is needed to detect higher
     * frequencies more precisely.
     *
     * @param tauEstimate
     * the estimated tau value.
     * @return a better, more precise tau value.
     */
    private fun parabolicInterpolation(tauEstimate: Int): Float {
        val s0: Float
        val s2: Float
        val x0 = if ((tauEstimate < 1)) tauEstimate else tauEstimate - 1
        val x2 = if ((tauEstimate + 1 < buffer.size)) tauEstimate + 1 else tauEstimate
        if (x0 == tauEstimate) return if ((buffer[tauEstimate] <= buffer[x2])) tauEstimate.toFloat() else x2.toFloat()
        if (x2 == tauEstimate) return if ((buffer[tauEstimate] <= buffer[x0])) tauEstimate.toFloat() else x0.toFloat()
        s0 = buffer[x0]
        val s1 = buffer[tauEstimate]
        s2 = buffer[x2]
        //fixed AUBIO implementation, thanks to Karl Helgason:
        //(2.0f * s1 - s2 - s0) was incorrectly multiplied with -1
        return tauEstimate + 0.5f * (s2 - s0) / (2.0f * s1 - s2 - s0)
    }

    /**
     * The main flow of the YIN algorithm. Returns a pitch value in Hz or -1 if
     * no pitch is detected using the current values of the input buffer.
     *
     * @return a pitch value in Hz or -1 if no pitch is detected.
     */
    fun getPitch(aInputBuffer: ShortArray): Float {
        inputBuffer = aInputBuffer
        buffer = FloatArray(inputBuffer.size / 2)
        var pitchInHertz = -1f

        //step 2
        difference()

        //step 3
        cumulativeMeanNormalizedDifference()

        //step 4
        val tauEstimate: Int = absoluteThreshold()

        //step 5
        if (tauEstimate != -1) {
            val betterTau = parabolicInterpolation(tauEstimate)

            //step 6
            //TODO Implement optimization for the YIN algorithm.
            //0.77% => 0.5% error rate,
            //using the data of the YIN paper
            //bestLocalEstimate()

            //conversion to Hz
            pitchInHertz = sampleRate / betterTau
        }

        return pitchInHertz
    } //

    /**
     * The interface to use to react to detected pitches.
     * @author Joren Six
     */
    interface DetectedPitchHandler {
        /**
         * Use this method to react to detected pitches.
         * The handleDetectedPitch is called for every sample even when
         * there is no pitch detected: in that case -1 is the pitch value.
         * @param time in seconds
         * @param pitch in Hz
         */
        fun handleDetectedPitch(time: Float, pitch: Float)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @Throws(IOException::class)
    fun processStream(record: AudioRecord, handler: DetectedPitchHandler?, channel: Channel<MutableList<Float>> = Channel()) {
        coroutine.launch {
            var pitchHandler = handler
            val format: AudioFormat = record.getFormat()
            val sampleRate = format.sampleRate.toFloat()
            val frameSize: Double = format.frameSizeInBytes.toDouble()
            val frameRate: Double = format.sampleRate.toDouble()
            var time: Float

            //by default use the print pitch handler
            if (pitchHandler == null) pitchHandler = printDetectPitchHandler

            //number of bytes / frameSize * frameRate gives the number of seconds
            //because we use float buffers there is a factor 2: 2 bytes per float?
            //Seems to be correct but a float uses 4 bytes: confused programmer is confused.
            val timeCalculationDivider = (frameSize * frameRate / 2).toFloat()
            var floatsProcessed: Long = 0
            instance = Yin(sampleRate)
            instance?.isRunning = true
            isRunning = true
            val bufferStepSize: Int = instance!!.bufferSize - instance!!.overlapSize
            println("buffer step size: $bufferStepSize")

            //read full buffer
            var hasMoreBytes = record.read(instance!!.inputBuffer, 0, instance!!.bufferSize) != -1
            println("has more bytes: $hasMoreBytes")
            floatsProcessed += instance!!.inputBuffer.size
            println("float processed: $floatsProcessed")
            val hertzToSent = mutableListOf<Float>()
            while (hasMoreBytes && isRunning) {
                val pitch: Float = instance!!.getPitch(inputBuffer)
                time = floatsProcessed / timeCalculationDivider
                pitchHandler.handleDetectedPitch(time, pitch)
                if (pitch > -1.0f) {
                    hertzToSent.add(pitch)
                }
                if (hertzToSent.isNotNullOrEmpty() && pitch <= -1.0f) {
                    channel.trySend(hertzToSent)
                    print("saved pitch:")
                    hertzToSent.forEach {
                        print("$it, ")
                        Timber.tag("SavedPitch").e("saved pitch: $it")
                    }
                    hertzToSent.clear()
                }
                //slide buffer with predefined overlap
                for (i in 0 until bufferStepSize) {
                    instance!!.inputBuffer[i] = instance!!.inputBuffer[i + instance!!.overlapSize]
                }
                hasMoreBytes = record.read(instance!!.inputBuffer, instance!!.overlapSize, bufferStepSize) != -1
                floatsProcessed += bufferStepSize.toLong()
            }
        }
    }

    companion object {
        val printDetectPitchHandler: DetectedPitchHandler = object : DetectedPitchHandler {
            override fun handleDetectedPitch(time: Float, pitch: Float) {
                println(time.toString() + "\t" + pitch)
            }
        }
    }
}