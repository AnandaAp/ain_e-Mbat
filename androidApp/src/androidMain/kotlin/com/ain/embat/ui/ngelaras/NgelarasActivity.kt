package com.ain.embat.ui.ngelaras

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.ain.embat.base.BaseActivity
import com.ain.embat.navigation.NgelarasRecordNav
import com.ain.embat.viewmodel.NgelarasRecordViewModel
import moe.tlaster.precompose.flow.collectAsStateWithLifecycle
import org.koin.core.component.inject

class NgelarasActivity: BaseActivity() {
    private val viewModel: NgelarasRecordViewModel by inject()

    override fun onDestroy() {
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        viewModel.releaseAudio()
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @Composable
    override fun InitiateUI() {
        super.InitiateUI()
        hideSystemUI()
//        val viewModel: NgelarasRecordViewModel by KoinJavaComponent.inject(clazz = NgelarasRecordViewModel::class.java)
        val pitch = viewModel.pitch.collectAsStateWithLifecycle()
        val hertz = viewModel.hertzValues.collectAsStateWithLifecycle()
        NgelarasRecordNav(
            viewModel = viewModel,
            pitch = pitch.value
        )
    }

    @Composable
    override fun ConfigureScreenOrientation() {
        val activity = LocalContext.current as Activity
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }
}