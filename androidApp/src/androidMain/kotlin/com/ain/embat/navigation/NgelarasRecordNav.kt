package com.ain.embat.navigation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.ain.embat.viewmodel.NgelarasRecordViewModel
import org.koin.java.KoinJavaComponent.inject
import ui.ngelaras.BaseLandscapeNgelaras

@Composable
fun NgelarasRecordNav() {
    val viewModel: NgelarasRecordViewModel by inject(clazz = NgelarasRecordViewModel::class.java)
    var recordButtonState by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->

    }
    BaseLandscapeNgelaras(onRecordButtonClick = {
        recordButtonState = !recordButtonState
        viewModel.onRecordButtonClicked(launcher, recordButtonState)
    })
}