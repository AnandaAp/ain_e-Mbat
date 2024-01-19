package com.ain.embat.navigation

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.ain.embat.viewmodel.NgelarasRecordViewModel
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import ui.ngelaras.BaseLandscapeNgelaras

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun NgelarasRecordNav(viewModel: NgelarasRecordViewModel) {
//    val viewModel: NgelarasRecordViewModel by inject(clazz = NgelarasRecordViewModel::class.java)
    var recordButtonState by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {}
    )
    val pitch = viewModel.pitch.collectAsStateWithLifecycle()
    BaseLandscapeNgelaras(
        onRecordButtonClick = {
            recordButtonState = !recordButtonState
            viewModel.onRecordButtonClicked(launcher, recordButtonState)
        },
        pitch = pitch.value
    )
}