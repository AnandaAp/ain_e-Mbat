package com.ain.embat.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import com.ain.embat.viewmodel.NgelarasRecordViewModel
import constants.AppConstant
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import ui.ngelaras.BaseLandscapeNgelaras

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun NgelarasRecordNav(
    viewModel: NgelarasRecordViewModel,
    pitch: String = AppConstant.DEFAULT_STRING_VALUE,
    hertz: Float = AppConstant.DEFAULT_FLOAT_VALUE
) {
    var recordButtonState by remember { mutableStateOf(false) }
    var isAlertDialogVisible by remember { mutableStateOf(false) }
    val isRecallPermissionReq by viewModel.isRecallPermissionReq.collectAsStateWithLifecycle()

    BaseLandscapeNgelaras(
        onRecordButtonClick = {
            recordButtonState = !recordButtonState
            viewModel.onRecordButtonClicked(state = recordButtonState)
        },
        pitch = pitch,
        hertz = hertz,
        buttonState = recordButtonState && !isAlertDialogVisible
    )

    if (isRecallPermissionReq) {
        isAlertDialogVisible = true
        BasicAlertDialog(
            onDismissRequest = { viewModel.updateRecallPermissionRequestStatus(false) },
            modifier = Modifier.wrapContentSize(),
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                securePolicy = SecureFlagPolicy.SecureOn
            )
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(percent = 15)
                    )
                    .padding(all = 14.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(text = "Mohon bantuannya untuk mengizinkan Izin rekam suara / microphone di pengaturan")
                    Button(
                        onClick = { viewModel.openApplicationSetting() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Setuju")
                    }
                }
            }
        }
    } else isAlertDialogVisible = false
}