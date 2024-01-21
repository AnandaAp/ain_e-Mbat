package ui.custom

import androidx.compose.runtime.Composable
import states.Status

@Composable
fun AinStatusHandler(
    status: Status,
    onSuccessCallBack: @Composable () -> Unit = {},
    onFailedCallBack: @Composable () -> Unit = {},
    onDefaultCallBack: @Composable () -> Unit = {}
) {
    when (status) {
        Status.Success -> onSuccessCallBack()
        Status.Failed -> onFailedCallBack()
        else -> onDefaultCallBack()
    }
}