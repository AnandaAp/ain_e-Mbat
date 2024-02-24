package viewmodel

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

class StarterViewModel: ViewModel(), KoinComponent {
    private val context: Context by inject()
    private val _permissionStatus = MutableStateFlow(false)
    private val channel = Channel<Boolean>()
    val permissions = arrayOf(
        "android.permission.RECORD_AUDIO",
        "android.permission.READ_MEDIA_AUDIO"
    )
    val permissionStatus = _permissionStatus.asStateFlow()
    fun checkPermission(
        callback: (status: Boolean) -> Unit
    ) {
        viewModelScope.launch {
            checkAndRequestRecordPermission(
                context = context,
                permissions = permissions,
                onCallBack = { permissionGranted ->
                    callback(permissionGranted)
                    channel.send(true)
                }
            )
        }
    }

    fun updatePermissionStatus(status: Boolean) {
        viewModelScope.launch {
            if (channel.receive()) {
                _permissionStatus.update { status }
                Timber.tag(StarterViewModel::class.simpleName).e("Permission Status: ${permissionStatus.value}")
            }
        }
    }
    private suspend fun checkAndRequestRecordPermission(
        context: Context,
        vararg permissions: String,
        onCallBack: suspend (Boolean) -> Unit = {  }
    ) {
        val permissionCheckResult = permissions.all { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
        if (permissionCheckResult) {
            onCallBack(true)
        } else {
            onCallBack(false)
        }
    }
}