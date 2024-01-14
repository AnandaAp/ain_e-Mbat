package com.ain.embat.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Environment
import android.os.Parcel
import androidx.core.content.ContextCompat
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter.writeParcel
import constants.AppConstant
import constants.DefaultTensorFlow
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.plus
import org.json.JSONObject
import org.koin.core.component.KoinComponent
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

@SuppressLint("MissingPermission")
class SingRecorder(
    context: Context,
    private val key: String = AppConstant.DEFAULT_STRING_VALUE,
    numberRecordings: Int = AppConstant.DEFAULT_NUMBER_OF_RECORD
): KoinComponent {
    private val AUDIO_SOURCE = MediaRecorder.AudioSource.VOICE_RECOGNITION
    private val tag = "AinRecord"
    private val SAMPLE_RATE = 16000
    private val CHANNEL_MASK = AudioFormat.CHANNEL_IN_MONO
    private val ENCODING = AudioFormat.ENCODING_PCM_16BIT
    private val BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_MASK, ENCODING)
    private var buffer = ShortArray(BUFFER_SIZE)
    private var bufferForInference: ArrayList<Short> = arrayListOf()
    private val AUDIO_FORMAT =
        AudioFormat.Builder()
            .setEncoding(ENCODING)
            .setSampleRate(SAMPLE_RATE)
            .setChannelMask(CHANNEL_MASK)
            .build()
    private var isRecording = false
    var pcmStream: ByteArrayOutputStream = ByteArrayOutputStream()
    var isDone = false

    //private boolean cancelled;
    private val minimumVoice = 100
    private val maximumSilence = 700
    private val upperLimit = 100

    //sample
    private val sampleLength = DoubleArray(numberRecordings)
    private val sampleToken = 0

    //thread
    private val coroutine: CoroutineScope = CoroutineScope(Dispatchers.IO) + CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
    }

//    @get:SuppressLint("MissingPermission")
    private lateinit var recorder: AudioRecord

    private val isPermissionGranted =  ContextCompat
        .checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

    suspend fun startRecording() {
        if (isPermissionGranted) {
            Timber.tag(tag).e("start the recording")
            recorder = AudioRecord
                .Builder()
                .setAudioSource(AUDIO_SOURCE)
                .setAudioFormat(AUDIO_FORMAT)
                .setBufferSizeInBytes(BUFFER_SIZE)
                .build()
            recorder.startRecording()
            isRecording = coroutine.async {
                readAudio.run()
                return@async true
            }.await()
        }
    }
    suspend fun stopRecording(): ByteArrayOutputStream {
        if (recorder != null && recorder.state == AudioRecord.STATE_INITIALIZED) {
            Timber.tag(tag).e("stop the recording")
            isRecording = false
            recorder.stop()
            recorder.release()
            isDone = true
        }
        return pcmStream
    }

    fun stopRecordingForInference(): ArrayList<Short> {
        return bufferForInference
    }

    private val readAudio = Runnable {
        var readBytes: Int
        while (isRecording) {
            readBytes = recorder!!.read(buffer, 0, BUFFER_SIZE)

            //Higher volume of microphone
            //https://stackoverflow.com/questions/25441166/how-to-adjust-microphone-sensitivity-while-recording-audio-in-android
            if (readBytes > 0) {
                for (i in 0 until readBytes) {
                    buffer[i] = (buffer[i] * 6.7)
                        .toInt()
                        .coerceAtMost(Short.MAX_VALUE.toInt())
                        .toShort()
                }
            }
            if (readBytes != AudioRecord.ERROR_INVALID_OPERATION) {
                for (s in buffer) {

                    // Add all values to arraylist
                    bufferForInference.add(s)

                    writeShort(pcmStream, s)
                }
            }
        }
    }

    @Throws(IOException::class)
    fun pcmToWav(byteArrayOutputStream: ByteArrayOutputStream): ByteArray {
        val stream = ByteArrayOutputStream()
        val pcmAudio = byteArrayOutputStream.toByteArray()
        writeString(stream, "RIFF") // chunk id
        writeInt(stream, 36 + pcmAudio.size) // chunk size
        writeString(stream, "WAVE") // format
        writeString(stream, "fmt ") // subchunk 1 id
        writeInt(stream, 16) // subchunk 1 size
        writeShort(stream, 1.toShort()) // audio format (1 = PCM)
        writeShort(stream, 1.toShort()) // number of channels
        writeInt(stream, SAMPLE_RATE) // sample rate
        writeInt(stream, SAMPLE_RATE * 2) // byte rate
        writeShort(stream, 2.toShort()) // block align
        writeShort(stream, 16.toShort()) // bits per sample
        writeString(stream, "data") // subchunk 2 id
        writeInt(stream, pcmAudio.size) // subchunk 2 size
        stream.write(pcmAudio)
        val parcel = Parcel.obtain()
        parcel.writeByteArray(stream.toByteArray())
        writeParcel(parcel, DefaultTensorFlow.BYTE_OUTPUT_ID, parcel, false)
//        parcel.writeByteArray(stream.toByteArray())
//        writeParcel(parcel, WAV_ID, parcel, false)
//        val pcmAudio = byteArrayOutputStream.toByteArray()
//        writeString(parcel, 92389,"RIFF", false) // chunk id
//        writeInt(parcel, 3123, 36 + pcmAudio.size) // chunk size
//        writeString(parcel, 8830, "WAVE", false) // format
//        writeString(parcel, 8239, "fmt ", false) // subchunk 1 id
//        writeInt(parcel, 8290, 16) // subchunk 1 size
//        writeShort(parcel, 6819, 1.toShort()) // audio format (1 = PCM)
//        writeShort(parcel, 6661, 1.toShort()) // number of channels
//        writeInt(parcel, 721, SAMPLE_RATE) // sample rate
//        writeInt(parcel, 882, SAMPLE_RATE * 2) // byte rate
//        writeShort(parcel, 81, 2.toShort()) // block align
//        writeShort(parcel, 599, 16.toShort()) // bits per sample
//        writeString(parcel, 90112, "data", false) // subchunk 2 id
//        writeInt(parcel, 878, pcmAudio.size) // subchunk 2 size
//        stream.write(pcmAudio)

        return stream.toByteArray()
    }

    /**
     * Write a 32-bit integer to an output stream, in Little Endian format.
     *
     * @param output Output stream
     * @param value  Integer value
     */
    private fun writeInt(output: ByteArrayOutputStream, value: Int) {
        output.write(value)
        output.write(value shr 8)
        output.write(value shr 16)
        output.write(value shr 24)
    }

    /**
     * Write a 16-bit integer to an output stream, in Little Endian format.
     *
     * @param output Output stream
     * @param value  Integer value
     */
    private fun writeShort(
        output: ByteArrayOutputStream,
        value: Short
    ) {
        output.write(value.toInt())
        output.write(value.toInt() shr 8)
    }

    /**
     * Write a string to an output stream.
     *
     * @param output Output stream
     * @param value  String value
     */
    private fun writeString(
        output: ByteArrayOutputStream,
        value: String
    ) {
        for (element in value) {
            output.write(element.code)
        }
    }

    /**
     * Generate a JSON config for the hotword.
     *
     * @return JSONObject containing config.
     */
    private fun generateConfig(): JSONObject {
        val obj = JSONObject()
        try {
            obj.put("hotword_key", key)
            obj.put("kind", "personal")
            obj.put("dtw_ref", 0.22)
            obj.put("from_mfcc", 1)
            obj.put("to_mfcc", 13)
            obj.put("band_radius", 10)
            obj.put("shift", 10)
            obj.put("window_size", 10)
            obj.put("sample_rate", SAMPLE_RATE)
            obj.put("frame_length_ms", 25.0)
            obj.put("frame_shift_ms", 10.0)
            obj.put("num_mfcc", 13)
            obj.put("num_mel_bins", 13)
            obj.put("mel_low_freq", 20)
            obj.put("cepstral_lifter", 22.0)
            obj.put("dither", 0.0)
            obj.put("window_type", "povey")
            obj.put("use_energy", false)
            obj.put("energy_floor", 0.0)
            obj.put("raw_energy", true)
            obj.put("preemphasis_coefficient", 0.97)
            return obj
        } catch (e: Exception) {
            return obj.put("exception", e.localizedMessage)
        }
    }

    /**
     * Write a wav file from the current sample.
     *
     * @throws IOException
     */
    fun writeWav(byteArrayOutputStream: ByteArrayOutputStream) {
        var wav = ByteArray(0)
        try {
            wav = pcmToWav(byteArrayOutputStream)
            //Log.e("WAV_size", wav.size.toString())
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var stream: FileOutputStream? = null
        try {
            try {
                Timber.i("Check if the wav file exist")
                stream = FileOutputStream(
                    Environment.getExternalStorageDirectory().toString() +
                            "/Recordings/Pitch Estimator/soloupis.wav", false
                )
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            try {
                Timber.i("the wav file is not exist, try to write it")
                stream?.write(wav)
            } catch (e: Exception) {
                Timber.tag("PERMISSIONS").e("NOT ABLE TO WRITE .WAV TO SDCARD")
            }
        } finally {
            if (stream != null) {
                try {
                    stream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun reInitializePcmStream() {
        pcmStream = ByteArrayOutputStream()
        bufferForInference = arrayListOf()
    }
}

