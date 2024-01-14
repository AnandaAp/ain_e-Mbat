package com.ain.embat.viewmodel

import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import android.os.Handler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.content.ContextCompat
import com.ain.embat.utils.PitchModelExecutor
import com.ain.embat.utils.SingRecorder
import constants.AppConstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.inject
import timber.log.Timber
import viewmodel.basic.BaseViewModel
import java.io.ByteArrayOutputStream
import java.io.File

class NgelarasRecordViewModel: BaseViewModel() {
    private val context: Context by inject()
    private val recorder: SingRecorder by inject()
    private val pitchModelExecutor: PitchModelExecutor by inject()
    private val _isRecorded = MutableStateFlow(false)
    private val _hertzValues = MutableStateFlow(doubleArrayOf())
    private val _inputTextFromAssets = MutableStateFlow(AppConstant.DEFAULT_STRING_VALUE)
    private val _inferenceDone = MutableStateFlow(false)

    val isRecorded = _isRecorded.asStateFlow()
    val hertzValues = _hertzValues.asStateFlow()
    val inputTextFromAssets = _inputTextFromAssets.asStateFlow()
    val inferenceDone = _inferenceDone.asStateFlow()

    private val updateLoopSingingHandler = Handler()

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
                println("callback: $callback")
                if (callback) {
                    when (state) {
                        true -> startRecording()
                        false -> stopRecording()
                    }
                }
            }
        }
    }

    private suspend fun startRecording() {
        _isRecorded.update { true }
        recorder.startRecording()
    }

    private suspend fun stopRecording() {

        val stream = recorder.stopRecording()
        val streamForInference = recorder.stopRecordingForInference()

        Timber.i("Stream size: %s", streamForInference.size.toString())
        Timber.i("Stream value: %s", streamForInference.takeLast(100).toString())

        _isRecorded.update { false }
        // Background thread to do inference with the generated short arraylist
        viewModelScope.launch {
            doInference(stream, streamForInference)
        }
    }

    private suspend fun doInference(
        stream: ByteArrayOutputStream,
        arrayListShorts: ArrayList<Short>
    ) = withContext(Dispatchers.IO) {
        // write .wav file to external directory
        recorder.writeWav(stream)
        // reset stream
        recorder.reInitializePcmStream()

        // The input must be normalized to floats between -1 and 1.
        // To normalize it, we just need to divide all the values by 2**16 or in our code, MAX_ABS_INT16 = 32768
        val floatsForInference = FloatArray(arrayListShorts.size)
        for ((index, value) in arrayListShorts.withIndex()) {
            floatsForInference[index] = (value / 32768F)
        }

        Timber.i("FLOATS\n%s", floatsForInference.takeLast(100).toString())

        // Inference
        _inferenceDone.update { false }
        Timber.i("HERTZ\n%s", hertzValues.value)
        _inferenceDone.update { true }
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
           launcher.launch(permissions as Array<String>?)
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

    fun setUpdateLoopSingingHandler() {
        // Start loop for collecting sound and inferring
//        updateLoopSingingHandler.postDelayed(updateLoopSingingRunnable, 0)
    }
}