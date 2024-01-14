package com.ain.embat.viewmodel

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.content.ContextCompat
import com.ain.embat.utils.TensorFlowModeler
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import viewmodel.basic.BaseViewModel

class NgelarasRecordViewModel: BaseViewModel() {
    private val tensorFlow: TensorFlowModeler by inject()
    private val context: Context by inject()

    fun onRecordButtonClicked(
        launcher: ManagedActivityResultLauncher<String, Boolean>,
        state: Boolean
    ) {
        viewModelScope.launch {
            checkAndRequestRecordPermission(
                context = context,
                launcher = launcher
            ) { callback ->
                if (callback) {
                    when (state) {
                        true -> tensorFlow.startRecording()
                        false -> tensorFlow.stopRecording()
                    }
                }
            }
        }
    }

   private suspend fun checkAndRequestRecordPermission(
        context: Context,
        permission: String = "android.permission.RECORD_AUDIO",
        launcher: ManagedActivityResultLauncher<String, Boolean>,
        onCallBack: suspend (Boolean) -> Unit = {  }
    ) {
        val permissionCheckResult = ContextCompat.checkSelfPermission(context, permission)
        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
            // start recording because permission is already granted
            onCallBack(true)
        } else {
            // Request a permission
            onCallBack(false)
            launcher.launch(permission)
        }
    }
}