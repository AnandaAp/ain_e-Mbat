package com.ain.embat.navigation

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.ain.embat.viewmodel.NgelarasRecordViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import constants.AppConstant
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import timber.log.Timber
import ui.ngelaras.BaseLandscapeNgelaras

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun NgelarasRecordNav(
    viewModel: NgelarasRecordViewModel,
    pitch: String = AppConstant.DEFAULT_STRING_VALUE,
    hertz: Float = AppConstant.DEFAULT_FLOAT_VALUE
) {
    var recordButtonState by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {}
    )
    val isRecallPermissionReq by viewModel.isRecallPermissionReq.collectAsStateWithLifecycle()
    val permissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    LaunchedEffect(key1 = isRecallPermissionReq) {
        Timber.tag("StarterViewModel").i("is recall permission request? $isRecallPermissionReq")
        if (isRecallPermissionReq) {
            permissionState.launchPermissionRequest()
        }
    }
    BaseLandscapeNgelaras(
        onRecordButtonClick = {
            recordButtonState = !recordButtonState
            viewModel.onRecordButtonClicked(launcher, recordButtonState)
        },
        pitch = pitch,
        hertz = hertz,
        buttonState = recordButtonState
    )
}