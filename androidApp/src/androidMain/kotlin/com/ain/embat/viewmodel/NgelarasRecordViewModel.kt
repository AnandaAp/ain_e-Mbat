package com.ain.embat.viewmodel

import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Build
import android.os.Environment
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import constants.AppConstant
import constants.NgelarasConstant
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.AudioModel
import models.Gamelan
import org.koin.core.component.inject
import timber.log.Timber
import util.AudioProcessor
import util.GendherBarung
import util.isNotNullOrEmpty
import viewmodel.basic.BaseViewModel
import java.io.File

class NgelarasRecordViewModel: BaseViewModel() {
    private val context: Context by inject()
    private val storedKey = NgelarasConstant.NGELARAS_SELECTED_CATEGORY_GAMELAN
    private val _audioModel = AudioModel(
        audioSource = MediaRecorder.AudioSource.CAMCORDER,
        sampleRate = 16000,
        channelMask = AudioFormat.CHANNEL_IN_MONO,
        encoding = AudioFormat.ENCODING_PCM_16BIT,
        bufferSize = AudioRecord.getMinBufferSize (
            16000,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
    )
    private val audioProcessor = AudioProcessor(audioModel = _audioModel)
    private val _isRecorded = MutableStateFlow(false)
    private val _hertzValues = MutableStateFlow(floatArrayOf())
    private val _pitch = MutableStateFlow(AppConstant.DEFAULT_STRING_VALUE)
    private val channel = Channel<MutableList<Float>>()
    private val saveHertz = MutableStateFlow(mutableListOf<Float>())
    private val isOnline = MutableStateFlow(false)
    private val _gamelan = MutableStateFlow(Gamelan())

    val isRecorded = _isRecorded.asStateFlow()
    val hertzValues = _hertzValues.asStateFlow()
    val pitch = _pitch.asStateFlow()
    val gamelan = _gamelan.asStateFlow()

    init {
        if (runtimeCache.getString(storedKey).isNotNullOrEmpty()) {
            isOnline.update { true }
        }
        if (isOnline.value) {
            _gamelan.update { runtimeCache.get<Gamelan>(storedKey)!! }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun onRecordButtonClicked(
        launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
        state: Boolean
    ) {
        coroutine1.launch {
            generateFolder()
        }
        viewModelScope.launch {
            checkAndRequestRecordPermission(
                context = context,
                launcher = launcher,
                permissions = arrayOf(
                    "android.permission.RECORD_AUDIO",
                    "android.permission.READ_MEDIA_AUDIO"
                )
            ) { callback ->
                if (callback) {
                    when (state) {
                        true ->  startRecording()
                        false -> stopRecording()
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun startRecording() {
        _isRecorded.update { true }
        coroutine1.launch {
            audioProcessor.startRecording(channel = channel)
        }
        coroutine2.launch {
            pitchTransform(channel)
        }
    }

    private fun stopRecording() {
        if (isRecorded.value) {
            _isRecorded.update { false }
            audioProcessor.stopRecording()
        }
    }

    fun releaseAudio() {
        audioProcessor.release()
        channel.close()
    }

   private suspend fun checkAndRequestRecordPermission(
       context: Context,
       vararg permissions: String,
       launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
       onCallBack: suspend (Boolean) -> Unit = {  }
    ) {
       val permissionCheckResult = permissions.all { permission ->
           ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
       }
       val launchPermission = mutableListOf<String>()
       for (per in permissions) {
           launchPermission.add(per)
       }
       if (permissionCheckResult) {
           // start recording because permission is already granted
           onCallBack(true)
       } else {
           // Request a permission
           onCallBack(false)
           launcher.launch(launchPermission.toTypedArray<String>())
       }
    }

    private fun generateFolder() {
        val root = File(Environment.getExternalStorageDirectory(), "Recordings/Pitch Estimator")
        Timber.tag("root").e("root dir: ${root.absolutePath}")
        Timber.tag("root").e("is root exist: ${root.exists()}")
        if (!root.exists()) {
            root.mkdirs()
            Timber.tag("root").e("is root exist: ${root.exists()}")
        }
    }

    private suspend fun pitchTransform(channel: Channel<MutableList<Float>> = Channel()) {
        if (channel.receive().isNotNullOrEmpty()) {
            saveHertz.update { channel.receive() }
        }
        if (saveHertz.value.isNotNullOrEmpty()) {
            _hertzValues.update { saveHertz.value.toFloatArray() }
        }
        if (hertzValues.value.isNotEmpty()) {
            _hertzValues.value.forEach { hertz ->
                _pitch.update {
                    GendherBarung.Slendro.pitch(hertz)
                }
            }
        }
    }
}