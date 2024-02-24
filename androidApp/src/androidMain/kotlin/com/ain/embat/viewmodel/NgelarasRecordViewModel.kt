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
import constants.TitilarasConstant
import kotlinx.coroutines.Dispatchers
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
import util.getValueBasedFromCondition
import util.handler.handler.GendherBarung
import util.isNotNullOrEmpty
import viewmodel.basic.BaseViewModel
import java.io.File

class NgelarasRecordViewModel: BaseViewModel() {
    private val context: Context by inject()
    private val storedKey = NgelarasConstant.NGELARAS_SELECTED_GAMELAN
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
    private lateinit var audioProcessor: AudioProcessor
    private val _isRecorded = MutableStateFlow(false)
    private val _hertzValues = MutableStateFlow(floatArrayOf())
    private val _hertz = MutableStateFlow(AppConstant.DEFAULT_FLOAT_VALUE)
    private val _pitch = MutableStateFlow(AppConstant.DEFAULT_STRING_VALUE)
    private val channel = Channel<MutableList<Float>>()
    private val saveHertz = MutableStateFlow(mutableListOf<Float>())
    private val isOnline = MutableStateFlow(false)
    private val _gamelan = MutableStateFlow(Gamelan())
    private val _isDataFetched = MutableStateFlow(false)
    private val _isRecallPermissionReq = MutableStateFlow(false)

    val isRecorded = _isRecorded.asStateFlow()
    val hertzValues = _hertzValues.asStateFlow()
    val hertz = _hertz.asStateFlow()
    val pitch = _pitch.asStateFlow()
    val gamelan = _gamelan.asStateFlow()
    val isDataFetch = _isDataFetched.asStateFlow()
    val isRecallPermissionReq = _isRecallPermissionReq.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            fetch()
        }
    }

    override suspend fun fetch() {
        fetchConfig()
    }

    private fun fetchConfig() {
        if (!isDataFetch.value) {
            if (runtimeCache.getString(storedKey).isNotNullOrEmpty()) {
                isOnline.update { true }
            }
            if (isOnline.value) {
                _gamelan.update { runtimeCache.get<Gamelan>(storedKey)!! }
                _isDataFetched.update { true }
            }
            //Temporary, only for testing purpose
            isOnline.update { false }
            Timber.tag("NgelarasInit").d("is data get from online page: ${isOnline.value}")
            Timber.tag("NgelarasInit").d("gamelan data: ${gamelan.value}")
            Timber.tag("NgelarasInit").d("one of titilaras: ${gamelan.value.frequency.laras[">y"]}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun onRecordButtonClicked(
        launcher: ManagedActivityResultLauncher<String, Boolean>,
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
                Timber.tag("StarterViewModel").i("callback for Permission record audio check is $callback")
                if (callback) {
                    when (state) {
                        true ->  {
                            audioProcessor = AudioProcessor(audioModel = _audioModel)
                            startRecording()
                        }
                        false -> stopRecording()
                    }
                } else {
                    _isRecallPermissionReq.update { true }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun startRecording() {
        _isRecorded.update { true }
        coroutine1.launch {
            audioProcessor.startRecording(
                savedHertz = _hertz,
                transformHertzToPitch = ::pitchTransform
            )
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
       launcher: ManagedActivityResultLauncher<String, Boolean>,
       onCallBack: suspend (Boolean) -> Unit = {  }
    ) {
       val permissionCheckResult = ContextCompat.checkSelfPermission(context, "android.permission.RECORD_AUDIO") == PackageManager.PERMISSION_GRANTED
       Timber.tag("StarterViewModel").i("Permission record audio check is $permissionCheckResult")
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
           launcher.launch("android.permission.RECORD_AUDIO")
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

    private fun pitchTransform() {
        if (hertz.value > -1.0f) {
            _pitch.update { pitchConverterHandler(hertz = hertz.value) }
            Timber.tag("SavedPitch").e("saved pitch: ${hertz.value}")
        }
    }

    private suspend fun handlePitchTransformOfMutableListOfFloat(channel: Channel<MutableList<Float>>) {
        if (channel.receive().isNotNullOrEmpty()) {
            saveHertz.update { channel.receive() }
        }
        if (saveHertz.value.isNotNullOrEmpty()) {
            _hertzValues.update { saveHertz.value.toFloatArray() }
        }
        if (hertzValues.value.isNotEmpty()) {
            _hertzValues.value.forEach { hertz ->
                _pitch.update {
                    pitchConverterHandler(hertz = hertz)
                }
            }
        }
    }

    private fun pitchConverterHandler(hertz: Float): String {
        return getValueBasedFromCondition(
            condition = isOnline.value,
            trueValue = onlinePitchConverterHandler(hertz),
            falseValue = offlinePitchConverterHandler(hertz)
        )
    }

    private fun onlinePitchConverterHandler(hertz: Float): String {
        return AppConstant.DEFAULT_STRING_VALUE
    }

    private fun offlinePitchConverterHandler(hertz: Float): String {
        return when (gamelan.value.frequency.titilaras.lowercase()) {
            TitilarasConstant.SLENDRO -> slendroPitchConvertHandler(hertz = hertz)
            TitilarasConstant.PELOK.NEM, TitilarasConstant.PELOK.BARANG -> pelogPitchConvertHandler(hertz = hertz)
            else -> AppConstant.DEFAULT_STRING_VALUE
        }
    }

    private fun slendroPitchConvertHandler(hertz: Float): String =
        when(gamelan.value.name.lowercase()) {
            constants.GendherBarungConstant.NAME -> GendherBarung.Slendro.pitch(hertz = hertz)
            else -> AppConstant.DEFAULT_STRING_VALUE
        }

    private fun pelogPitchConvertHandler(hertz: Float): String =
        when (gamelan.value.frequency.titilaras.lowercase()) {
            TitilarasConstant.PELOK.NEM -> pelogNemConverter(hertz)
            TitilarasConstant.PELOK.BARANG -> AppConstant.DEFAULT_STRING_VALUE
            else -> AppConstant.DEFAULT_STRING_VALUE
        }

    private fun pelogNemConverter(hertz: Float): String =
        when(gamelan.value.name.lowercase()) {
            constants.GendherBarungConstant.NAME -> GendherBarung.PelogNem.pitch(hertz = hertz)
            else -> AppConstant.DEFAULT_STRING_VALUE
        }
}